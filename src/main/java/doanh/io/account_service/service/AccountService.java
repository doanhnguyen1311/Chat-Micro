package doanh.io.account_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.mapper.AccountMapper;
import doanh.io.account_service.mapper.ProviderInfoMapper;
import doanh.io.account_service.mapper.SettingsMapper;
import doanh.io.account_service.mapper.UserProfileMapper;
import doanh.io.account_service.repository.AccountRepository;
import doanh.io.account_service.security.PasswordUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference; // Đảm bảo dòng này đúng

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final SettingsMapper settingsMapper;
    private final UserProfileMapper userProfileMapper;
    private final ProviderInfoMapper providerInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    private final ObjectMapper objectMapper;

    public APIResponse<?> getAll() {
        String redisKey = "accounts_list_dto_bytes";
        List<AccountDTO> accountDTOS = null;

        try {
            byte[] cachedBytes = (byte[]) redisTemplate.opsForValue().get(redisKey);
            if (cachedBytes != null) {
                log.info("✅ Lấy danh sách tài khoản từ Redis Cache (binary)");

                accountDTOS = objectMapper.readValue(
                        cachedBytes,
                        new TypeReference<List<AccountDTO>>() {}
                );

                return APIResponse.builder()
                        .data(accountDTOS)
                        .success(true)
                        .statusCode(200)
                        .message("Get All Accounts from Redis (binary)")
                        .build();
            }
        } catch (Exception e) {
            log.error("❌ Lỗi khi đọc hoặc giải mã binary từ Redis: {}", e.getMessage(), e);
        }

        log.info("⚠️ Không có cache, lấy từ DB");
        try {
            var accounts = accountRepository.findAll();

            accountDTOS = accounts.stream()
                    .map(accountMapper::toAccountDTO)
                    .toList();

            try {
                byte[] dataBytes = objectMapper.writeValueAsBytes(accountDTOS);
                redisTemplate.opsForValue().set(redisKey, dataBytes, 10, TimeUnit.MINUTES);
                log.info("📦 Lưu danh sách tài khoản vào Redis Cache (binary)");
            } catch (Exception e) {
                log.error("❌ Lỗi khi ghi binary vào Redis: {}", e.getMessage(), e);
            }

            return APIResponse.builder()
                    .data(accountDTOS)
                    .success(true)
                    .statusCode(200)
                    .message("Get All Accounts from DB")
                    .build();

        } catch (Exception e) {
            log.error("❌ Lỗi khi truy vấn DB: {}", e.getMessage(), e);

            return APIResponse.builder()
                    .success(false)
                    .statusCode(500)
                    .message("Internal Server Error")
                    .build();
        }
    }


    public APIResponse<?> getOne(String id) {
        String redisKey = "account:" + id;
        AccountDTO accountDTO = null;

        try {
            // 🔍 Thử lấy từ Redis trước
            String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);
            if (cachedJson != null) {
                log.info("✅ Lấy user {} từ Redis cache", id);

                accountDTO = objectMapper.readValue(cachedJson, AccountDTO.class);

                return APIResponse.builder()
                        .data(accountDTO)
                        .success(true)
                        .statusCode(200)
                        .message("Get Account by ID (from cache)")
                        .build();
            }
        } catch (Exception e) {
            log.error("❌ Lỗi khi đọc từ Redis cho account {}: {}", id, e.getMessage(), e);
        }

        // ⛏ Nếu không có trong cache, truy vấn từ DB
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        accountDTO = accountMapper.toAccountDTO(account);

        try {
            // 💾 Lưu lại vào Redis
            String json = objectMapper.writeValueAsString(accountDTO);
            stringRedisTemplate.opsForValue().set(redisKey, json, 10, TimeUnit.MINUTES);
            log.info("📦 Lưu user {} vào Redis cache", id);
        } catch (Exception e) {
            log.error("❌ Lỗi khi ghi vào Redis cho account {}: {}", id, e.getMessage(), e);
        }

        return APIResponse.builder()
                .data(accountDTO)
                .success(true)
                .statusCode(200)
                .message("Get Account by ID (from DB)")
                .build();
    }


    @Transactional
    public APIResponse<?> add(AccountDTO accountDTO) {
        boolean existEmail = accountRepository.existsByEmail(accountDTO.getEmail());
        if (existEmail) {
            return new APIResponse<>(null, "Exist email!", 400, false);
        }

        boolean existUsername = accountRepository.existsByUsername(accountDTO.getUsername());

        if (existUsername) {
            return new APIResponse<>(null, "Exist username!", 400, false);
        }

        Account account = accountMapper.toAccount(accountDTO);
        account.setCreatedAt(LocalDateTime.now());
        account.setStatus("ACTIVE");
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setIsOnline(false);
        account.setSecurity(account.getSecurity()
                .builder()
                .tokenVersion(0)
                .isLocked(false)
                .isVerified(false)
                .build());

        Account saved = accountRepository.save(account);
        AccountDTO savedDTO = accountMapper.toAccountDTO(saved);

        return APIResponse.builder()
                .message("Account created!")
                .data(savedDTO)
                .statusCode(200)
                .success(true)
                .build();
    }


    @Transactional
    public APIResponse<?> update(String id, AccountDTO accountDTO) {
        var existing = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        // Cập nhật các trường
        existing.setUsername(accountDTO.getUsername());
        existing.setEmail(accountDTO.getEmail());
        existing.setPhoneNumber(accountDTO.getPhoneNumber());
        existing.setProfile(userProfileMapper.toEntity(accountDTO.getProfile()));
        existing.setSettings(settingsMapper.toEntity(accountDTO.getSettings()));
        existing.setProvider(providerInfoMapper.toEntity(accountDTO.getProvider()));

        Account updated = accountRepository.save(existing);
        return APIResponse.builder()
                .data(accountMapper.toAccountDTO(updated))
                .statusCode(200)
                .success(true)
                .build();
    }

    @Transactional
    public APIResponse<?> delete(String id) {
        Optional<Account> accountOpt = accountRepository.findById(id);

        if (accountOpt.isEmpty()) {
            return APIResponse.builder()
                    .success(false)
                    .message("Account not found with id: " + id)
                    .data(null)
                    .statusCode(400)
                    .build();
        }

        AccountDTO deletedDTO = accountMapper.toAccountDTO(accountOpt.get());
        accountRepository.deleteById(id);

        return APIResponse.builder()
                .success(true)
                .message("Account deleted!")
                .data(deletedDTO)
                .statusCode(200)
                .build();
    }

    @Transactional
    public APIResponse<?> updatePassword(String id, String oldPassword, String newPassword) {
        var existing = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        // Ở đây nên mã hóa nếu để chung với auth
        var bool = PasswordUtil.matches(oldPassword, existing.getPassword());

        if (bool) {
            return APIResponse.builder()
                    .success(false)
                    .message("Old password doesn't match!")
                    .data(null)
                    .statusCode(400)
                    .build();
        }
        existing.setPassword(newPassword);

        accountRepository.save(existing);

        return APIResponse.builder()
                .success(true)
                .message("Account updated!")
                .data(existing)
                .statusCode(200)
                .build();
    }

    @Transactional
    public APIResponse<?> getOneWithEmailOrPhoneOrUsername(String email, String phone, String username) {
        String input = "";

        if(username != "" && username != null && !username.isEmpty()) {
            input = username;
        }
        if(phone != "" && phone != null && !phone.isEmpty()) {
            input = phone;
        }
        if(email != "" && email != null && !email.isEmpty()) {
            input = email;
        }
        var existing = accountRepository.findByUsernameOrEmailOrPhoneNumber(input);

        if(existing == null) {
            return APIResponse.builder()
                    .success(false)
                    .message("Account not found!")
                    .data(null)
                    .statusCode(400)
                    .build();
        }

        var res = accountMapper.toAccountDTO(existing);

        return APIResponse.builder()
                .success(true)
                .message("Account found!")
                .data(res)
                .statusCode(200)
                .build();
    }
}
