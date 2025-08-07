package doanh.io.graph_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import doanh.io.graph_service.dto.SimpleJoinGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {
    // khóa bí mật
    private static final String SALT = "s=3kd,ms--3kdk993k';;d=3p[3[e=3[3[ld;3'f[e'g'rf;g;'r'f/gl,gfdf./%&*(#(@(@";

    // Thuật toán mã hóa. AES/CBC/PKCS5Padding là một lựa chọn mạnh và phổ biến.
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    // Vector khởi tạo (IV) có độ dài 16 byte cho AES
    private static final int IV_LENGTH_BYTE = 16;

    private static SecretKeySpec secretKey;

    @Value("${SECRET_KEY}")
    private String secretKeyValue;

    public String generateLinkToken(SimpleJoinGroup simpleJoinGroup) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .issuer("Link")
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("groupId", simpleJoinGroup.getGroupId())
                        .claim("reason", simpleJoinGroup.getReason())
                        .claim("inviteType", simpleJoinGroup.getInviteType())
                        .claim("inviterId", simpleJoinGroup.getInviterId())
                        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKeyValue));
            return jwsObject.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(secretKeyValue);
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);

        if (!verified) {
            return false;
        }

        if(isTokenExpiry(token)) {
            return false;
        }

        return true;
    }

    public boolean isTokenExpiry(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return !expiryTime.after(new Date());
        } catch (ParseException e) {
            return true;
        }
    }

    // Khối static để tạo khóa bí mật một lần duy nhất khi class được load
    static {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = sha.digest(SALT.getBytes(StandardCharsets.UTF_8));
            // Chỉ sử dụng 16, 24 hoặc 32 byte đầu tiên cho khóa AES (128, 192, or 256 bit)
            // Ở đây ta dùng 32 byte (256 bit) cho độ bảo mật cao nhất.
            key = Arrays.copyOf(key, 32);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (Exception e) {
            // Lỗi này không nên xảy ra nếu môi trường Java được cài đặt đúng
            throw new RuntimeException("Lỗi khi khởi tạo khóa bí mật", e);
        }
    }

    /**
     * Mã hóa một chuỗi UserId.
     *
     * @param userId Chuỗi UserId cần mã hóa.
     * @return Một chuỗi đã được mã hóa dưới dạng Base64, an toàn để lưu trong cookie.
     */
    public String encryptUserId(String userId) {
        try {
            // 1. Tạo một IV ngẫu nhiên
            byte[] iv = new byte[IV_LENGTH_BYTE];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 2. Khởi tạo Cipher để mã hóa
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            // 3. Mã hóa dữ liệu
            byte[] encryptedData = cipher.doFinal(userId.getBytes(StandardCharsets.UTF_8));

            // 4. Kết hợp IV và dữ liệu đã mã hóa (IV + encryptedData)
            byte[] combined = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            // 5. Trả về dưới dạng Base64
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            // Ghi log lỗi ở đây nếu cần
            System.err.println("Lỗi khi mã hóa UserId: " + e.getMessage());
            // Ném ra một ngoại lệ runtime vì đây là lỗi hệ thống nghiêm trọng
            throw new RuntimeException("Không thể mã hóa UserId", e);
        }
    }

    /**
     * Giải mã một chuỗi đã được mã hóa bằng hàm encryptUserId.
     *
     * @param encryptedUserId Chuỗi Base64 đã mã hóa.
     * @return Chuỗi UserId gốc.
     */
    public String decryptUserId(String encryptedUserId) {
        try {
            // 1. Giải mã Base64 để lấy lại mảng byte kết hợp (IV + encryptedData)
            byte[] combined = Base64.getDecoder().decode(encryptedUserId);

            // 2. Tách IV và dữ liệu mã hóa
            IvParameterSpec ivParameterSpec = new IvParameterSpec(combined, 0, IV_LENGTH_BYTE);
            byte[] encryptedData = Arrays.copyOfRange(combined, IV_LENGTH_BYTE, combined.length);

            // 3. Khởi tạo Cipher để giải mã
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            // 4. Giải mã dữ liệu
            byte[] decryptedData = cipher.doFinal(encryptedData);

            // 5. Chuyển về dạng String và trả về
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Lỗi này thường xảy ra nếu cookie bị chỉnh sửa hoặc sai định dạng
            System.err.println("Lỗi khi giải mã UserId (dữ liệu có thể đã bị thay đổi): " + e.getMessage());
            // Ném ra một ngoại lệ runtime để Controller có thể bắt và xử lý (vd: trả về lỗi 401 Unauthorized)
            throw new RuntimeException("Không thể giải mã UserId, token không hợp lệ", e);
        }
    }
}
