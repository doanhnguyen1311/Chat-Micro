package doanh.io.account_service.service;

import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.SecurityInfoDTO;
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.entity.info.SecurityInfo;
import doanh.io.account_service.mapper.SecurityMapper;
import doanh.io.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AccountRepository accountRepository;
    private final SecurityMapper securityMapper;

    @Transactional(readOnly = true)
    public APIResponse<?> getSecurityInfo(String email) {
        return accountRepository.findByEmail(email)
                .map(account -> {
                    SecurityInfoDTO dto = securityMapper.toDto(account.getSecurity());
                    return APIResponse.builder()
                            .data(dto)
                            .success(true)
                            .statusCode(200)
                            .message("Lấy thông tin bảo mật thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .data(null)
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với email: " + email)
                        .build());
    }

    @Transactional(readOnly = true)
    public APIResponse<?> getTokenVersion(String id) {
        return accountRepository.findById(id)
                .map(account -> APIResponse.builder()
                        .data(account.getSecurity().getTokenVersion())
                        .success(true)
                        .statusCode(200)
                        .message("Lấy token version thành công")
                        .build())
                .orElseGet(() -> APIResponse.builder()
                        .data(null)
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với id: " + id)
                        .build());
    }

    @Transactional
    public APIResponse<?> incrementLoginAttempts(String email) {
        return accountRepository.findByEmail(email)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setLoginAttempts(security.getLoginAttempts() + 1);

                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .data(null)
                            .success(true)
                            .statusCode(200)
                            .message("Tăng số lần đăng nhập thất bại thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .data(null)
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với email: " + email)
                        .build());
    }

    @Transactional
    public APIResponse<?> updateSecurity(String accountId, SecurityInfoDTO dto) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo updated = securityMapper.toEntity(dto);
                    account.setSecurity(updated);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .data(dto)
                            .success(true)
                            .statusCode(200)
                            .message("Cập nhật thông tin bảo mật thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .data(null)
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> increaseTokenVersion(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setTokenVersion(security.getTokenVersion() + 1);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Tăng token version thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> resetLoginAttempts(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setLoginAttempts(0);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Reset login attempts thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> lockAccount(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setIsLocked(true);
                    security.setLockTime(LocalDateTime.now());
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Tài khoản đã bị khoá")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> unlockAccount(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setIsLocked(false);
                    security.setLockTime(null);
                    security.setLoginAttempts(0);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Tài khoản đã được mở khoá")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> verifyEmail(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setIsVerified(true);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Xác thực email thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> enableMFA(String accountId, String type, String secret) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setMfaEnabled(true);
                    security.setMfaType(type);
                    security.setMfaSecret(secret);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Bật xác thực đa yếu tố thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

    @Transactional
    public APIResponse<?> disableMFA(String accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    SecurityInfo security = account.getSecurity();
                    security.setMfaEnabled(false);
                    security.setMfaType(null);
                    security.setMfaSecret(null);
                    account.setSecurity(security);
                    accountRepository.save(account);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Tắt xác thực đa yếu tố thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy tài khoản với ID: " + accountId)
                        .build());
    }

}
