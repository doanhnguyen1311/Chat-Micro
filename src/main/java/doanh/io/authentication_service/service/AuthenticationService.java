package doanh.io.authentication_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.openid.connect.sdk.LogoutRequest;
import doanh.io.account_service.grpc.*;
import doanh.io.authentication_service.config.EnvConfig;
import doanh.io.authentication_service.config.JwtConfig;
import doanh.io.authentication_service.dto.AccountDTO;
import doanh.io.authentication_service.dto.ProviderInfoDTO;
import doanh.io.authentication_service.dto.SettingsDTO;
import doanh.io.authentication_service.dto.UserProfileDTO;
import doanh.io.authentication_service.dto.request.LoginRequest;
import doanh.io.authentication_service.dto.response.APIResponse;
import doanh.io.authentication_service.dto.response.AuthenticatedResponse;
import doanh.io.authentication_service.entity.RefreshToken;
import doanh.io.authentication_service.repository.RefreshTokenRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthenticationService {

    private final AccountServiceGrpc.AccountServiceBlockingStub accountServiceBlockingStub;
    private final AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authenticationServiceBlockingStub;
    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthenticationService(JwtConfig jwtConfig, RefreshTokenRepository refreshTokenRepository, RedisTemplate<Object, Object> redisTemplate) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext().build();
        accountServiceBlockingStub = AccountServiceGrpc.newBlockingStub(channel);
        authenticationServiceBlockingStub = AuthenticationServiceGrpc.newBlockingStub(channel);
        this.jwtConfig = jwtConfig;
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
    }


    public APIResponse<?> getAccount(String id) {
        AccountIdRequest request = AccountIdRequest.newBuilder()
                .setId(id)
                .build();

        var data = accountServiceBlockingStub.getOne(request);

        var settingStub = data.getAccount().getSettings();

        var profileStub = data.getAccount().getProfile();

        var providerStub = data.getAccount().getProvider();

        var accountStub = data.getAccount();

        SettingsDTO settings = SettingsDTO.builder()
                .theme(settingStub.getTheme())
                .language(settingStub.getLanguage())
                .notificationsEnabled(settingStub.getNotificationsEnabled())
                .soundOn(settingStub.getSoundOn())
                .build();

        UserProfileDTO profiles = UserProfileDTO.builder()
                .bio(profileStub.getBio())
                .avatarUrl(profileStub.getAvatarUrl())
                .website(profileStub.getWebsite())
                .fullName(profileStub.getFullName())
                .gender(profileStub.getGender())
                .coverPhotoUrl(profileStub.getCoverPhotoUrl())
                .location(profileStub.getLocation())
                .build();

        ProviderInfoDTO providers = ProviderInfoDTO.builder()
                .provider(providerStub.getProvider())
                .providerId(providerStub.getProviderId())
                .build();

        AccountDTO dto = AccountDTO.builder()
                .email(accountStub.getEmail())
                .username(accountStub.getUsername())
                .id(accountStub.getId())
                .status(accountStub.getStatus())
                .phoneNumber(accountStub.getPhoneNumber())
                .profile(profiles)
                .provider(providers)
                .settings(settings)
                .build();


        return APIResponse.builder()
                .data(dto)
                .message("Success")
                .success(true)
                .statusCode(200)
                .build();

    }

    public String generateToken(AccountResponse user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getAccount().getUsername())
                        .issuer("User")
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("email", user.getAccount().getEmail())
                        .claim("scope", "USER")
                        .claim("userId", user.getAccount().getId())
                        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(jwtConfig.getSecretKey()));
            return jwsObject.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateRefreshToken(AccountResponse user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getAccount().getUsername())
                        .issuer("User")
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(60, ChronoUnit.DAYS)))
                        .jwtID(UUID.randomUUID().toString())
                        .claim("email", user.getAccount().getEmail())
                        .claim("scope", "USER")
                        .claim("userId", user.getAccount().getId())
                        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(jwtConfig.getSecretKey()));
            return jwsObject.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Transactional
    public APIResponse<AuthenticatedResponse> Login(LoginRequest request) {
        String username = request.getUsername();
        String deviceId = request.getDeviceId();

        var rq = doanh.io.account_service.grpc.AccountDTO.newBuilder()
                .setUsername(username)
                .build();

        var data = accountServiceBlockingStub.getOneWithEmailOrPhoneOrUsername(rq);

        // ❌ User không tồn tại
        if (!data.getSuccess()) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .message("Login failed!")
                    .data(null)
                    .build();
        }

        String userId = data.getAccount().getId();

        //  Kiểm tra nếu thiết bị đã bị block
        if (loginAttemptService.isBlocked(userId, deviceId)) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .statusCode(403)
                    .message("\n" +
                            "Device has been locked due to too many incorrect entries. Please try again in 24 hours.")
                    .data(null)
                    .build();
        }

        //  So sánh mật khẩu bằng BCrypt
        if (!passwordEncoder.matches(request.getPassword(), data.getAccount().getPassword())) {
            long failCount = loginAttemptService.incrementFail(userId, deviceId);
            boolean isDefault = isDefaultDevice(userId, deviceId);
            boolean hasDefault = hasDefaultDevice(userId);

            if (failCount >= 5) {
                if (!isDefault) {
                    if (!hasDefault) {
                        loginAttemptService.block(userId, deviceId);
                        return APIResponse.<AuthenticatedResponse>builder()
                                .success(false)
                                .statusCode(429)
                                .message("You have entered the wrong password too many times. Please try again after 24 hours!")
                                .data(null)
                                .build();
                    }
                    return APIResponse.<AuthenticatedResponse>builder()
                            .success(false)
                            .statusCode(401)
                            .message("You must login with default device!")
                            .data(null)
                            .build();
                } else {
                    return APIResponse.<AuthenticatedResponse>builder()
                            .success(false)
                            .statusCode(401)
                            .message("Login failed too many times! We will send a notification to the default device to check!")
                            .data(null)
                            .build();
                }
            }

            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .statusCode(401)
                    .message("Login failed!")
                    .data(null)
                    .build();
        } else {

            boolean isDefault = isDefaultDevice(userId, deviceId);
            boolean hasDefault = hasDefaultDevice(userId);
            if (!isDefault) {
                long failCount = loginAttemptService.incrementFail(userId, deviceId);
                if (failCount >= 5) {
                    if (!hasDefault) {
                        loginAttemptService.block(userId, deviceId);
                        return APIResponse.<AuthenticatedResponse>builder()
                                .success(false)
                                .statusCode(429)
                                .message("You have entered the wrong password too many times. Please try again after 24 hours!")
                                .data(null)
                                .build();
                    }
                    return APIResponse.<AuthenticatedResponse>builder()
                            .success(false)
                            .statusCode(401)
                            .message("You must login with default device!")
                            .data(null)
                            .build();
                }
            }
//            else {
//                return APIResponse.<AuthenticatedResponse>builder()
//                        .success(false)
//                        .statusCode(401)
//                        .message("Login failed too many times! We will send a notification to the default device to check!")
//                        .data(null)
//                        .build();
//            }
        }

        // ✅ Đăng nhập thành công → reset Redis
        loginAttemptService.reset(userId, deviceId);

        boolean hasDefault = hasDefaultDevice(userId);

        // 🧹 Xoá refresh token cũ nếu có
        refreshTokenRepository.deleteByUserIdAndDeviceId(userId, deviceId);

        // 🎟️ Tạo access token + refresh token
        var token = generateToken(data);
        var refreshToken = generateRefreshToken(data);

        var refreshBean = RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .deviceId(deviceId)
                .tokenVersion(1L)
                .createdAt(LocalDateTime.now())
                .expiresAt(Instant.now().plus(60, ChronoUnit.DAYS)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .isDefaultDevice(!hasDefault)
                .build();

        var resRef = refreshTokenRepository.save(refreshBean);

        if (resRef == null || token == null) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .statusCode(400)
                    .message("Login failed! Please try again.")
                    .data(null)
                    .build();
        }

        return APIResponse.<AuthenticatedResponse>builder()
                .success(true)
                .statusCode(200)
                .message("Login success!")
                .data(AuthenticatedResponse.builder()
                        .isAuthentication(true)
                        .token(token)
                        .build())
                .build();
    }


    public boolean isTokenExpiry(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return !expiryTime.after(new Date());
        } catch (ParseException e) {
            log.error("Failed to parse token for expiry check: {}", e.getMessage());
            return true;
        }
    }


    public APIResponse<?> verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtConfig.getSecretKey());
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);

        if (!verified) {
            return APIResponse.builder()
                    .success(false)
                    .message("Invalid token signature!")
                    .statusCode(401)
                    .build();
        }

        return APIResponse.builder()
                .success(true)
                .statusCode(200)
                .data(AuthenticatedResponse.builder()
                        .token(token)
                        .isAuthentication(true)
                        .build())
                .message("Token signature verified successfully!")
                .build();
    }

    private String userIdFromCookie(String refreshToken) throws ParseException {
        var decodeRefreshToken = SignedJWT.parse(refreshToken);

        var userIdLeader = decodeRefreshToken.getJWTClaimsSet().getClaim("userId");

        return (String) userIdLeader;
    }


    public APIResponse<AuthenticatedResponse> introspect(String token, String deviceId) throws ParseException, JOSEException {
        if (token == null || token.isBlank()) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .statusCode(401)
                    .message("Token is missing")
                    .data(null)
                    .build();
        }

        var userId = userIdFromCookie(token);

        // 🔐 Check block sớm
        if (loginAttemptService.isBlocked(userId, deviceId)) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .statusCode(403)
                    .message("Device has been locked due to too many incorrect entries. Please try again in 24 hours.")
                    .data(null)
                    .build();
        }

        var verify = verifyToken(token);
        var isExpiry = isTokenExpiry(token);

        if (!verify.isSuccess()) {
            // 👉 Ghi nhận lần fail (vì token sai có thể là tấn công)
            long failCount = loginAttemptService.incrementFail(userId, deviceId);
            if (failCount >= 5) {
                loginAttemptService.block(userId, deviceId);
                return APIResponse.<AuthenticatedResponse>builder()
                        .success(false)
                        .statusCode(429)
                        .message("Too many failed attempts with invalid token. Device has been blocked.")
                        .data(null)
                        .build();
            }

            return APIResponse.<AuthenticatedResponse>builder()
                    .success(false)
                    .message("Invalid token signature!")
                    .statusCode(401)
                    .data(null)
                    .build();
        }

// ✅ Nếu token hợp lệ → reset fail count
        loginAttemptService.reset(userId, deviceId);

        if (isExpiry) {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(true)
                    .statusCode(200)
                    .message("refresh")
                    .data(AuthenticatedResponse.builder()
                            .isAuthentication(true)
                            .token(refreshToken(token, deviceId))
                            .build())
                    .build();
        } else {
            return APIResponse.<AuthenticatedResponse>builder()
                    .success(true)
                    .statusCode(200)
                    .message("not refresh")
                    .data(AuthenticatedResponse.builder()
                            .isAuthentication(false)
                            .token(token)
                            .build())
                    .build();
        }
    }


    @Transactional
    public String refreshToken(String accessToken, String deviceId) throws ParseException, JOSEException {
        var apiRes = verifyToken(accessToken);
        if (!apiRes.isSuccess()) {
            return null;
        } else {

            SignedJWT signedJWT = SignedJWT.parse(accessToken);

            var userId = signedJWT.getJWTClaimsSet().getClaim("userId").toString();

            AccountIdRequest request = AccountIdRequest.newBuilder()
                    .setId(userId)
                    .build();

            var data = accountServiceBlockingStub.getOne(request);

            var refreshToken = generateRefreshToken(data);

            var refreshBean = RefreshToken.builder()
                    .userId(data.getAccount().getId())
                    .token(refreshToken)
                    .deviceId(deviceId)
                    .tokenVersion(1L) // hoặc null nếu chưa dùng
                    .createdAt(LocalDateTime.now())
                    .expiresAt(Instant.now().plus(60, ChronoUnit.DAYS)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                    )
                    .build();

            refreshTokenRepository.save(refreshBean);

            String token = generateToken(data);

            return token;
        }
    }

    public boolean isRateLimited(String username) {
        String key = "rate:login:" + username;
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }
        return count > 5; // Giới hạn 5 lần/phút
    }

    @Transactional
    public void logout(String userId, String deviceId) {
        refreshTokenRepository.deleteByUserIdAndDeviceId(userId, deviceId);
    }

    @Transactional
    public APIResponse<?> logoutAllDevices(String userId, String deviceId) {
        if (isDefaultDevice(userId, deviceId)) {
            refreshTokenRepository.deleteByUserIdAndIsDefaultDeviceFalse(userId);
            return APIResponse.<Void>builder()
                    .success(true)
                    .message("Logged out from all devices (except default) successfully.")
                    .statusCode(200)
                    .build();
        } else {
            return APIResponse.builder()
                    .success(false)
                    .message("Not default device!")
                    .statusCode(400)
                    .build();
        }
    }

    public boolean isDefaultDevice(String userId, String deviceId) {
        Optional<RefreshToken> defaultToken = refreshTokenRepository.findByUserIdAndIsDefaultDeviceTrue(userId);
        return defaultToken.isPresent() && defaultToken.get().getDeviceId().equalsIgnoreCase(deviceId);
    }

    public boolean hasDefaultDevice(String userId) {
        return refreshTokenRepository.existsByUserIdAndIsDefaultDeviceTrue(userId);
    }

    // gửi đường link tới thiết bị mặc định rồi kêu nó xác nhận để xóa key đó trong redis của thiết bị đanng bị khóa 1 ngày


}
