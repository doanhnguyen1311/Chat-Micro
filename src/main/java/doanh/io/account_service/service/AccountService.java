package doanh.io.account_service.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public APIResponse<?> getAll() {
        return APIResponse.builder()
                .data(accountRepository.findAll()
                        .stream()
                        .map(accountMapper::toAccountDTO)
                        .toList())
                .success(true)
                .statusCode(200)
                .message("Get All Accounts")
                .build();
    }

    public APIResponse<?> getOne(String id) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        return APIResponse.builder()
                .data(accountMapper.toAccountDTO(account))
                .success(true)
                .statusCode(200)
                .message("Get Account by ID")
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
}
