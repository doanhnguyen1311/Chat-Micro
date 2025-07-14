package doanh.io.account_service.service;

import doanh.io.account_service.dto.request.LoginRequest;
import doanh.io.account_service.dto.response.LoginResponse;
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.entity.info.SecurityInfo;
import doanh.io.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Account account = accountRepository.findByUsernameOrEmailOrPhoneNumber(request.getUsername());

        if (account == null) {
            return null;
        }

        if (!isPasswordValid(request.getPassword(), account)) {
            return null;
        }

        if (account.getSecurity().getIsLocked()) {
            return null;
        }

        SecurityInfo security = account.getSecurity();

        return LoginResponse.builder()
                .id(account.getId())
                .isMfaEnabled(security.getMfaEnabled())
                .mfaType(security.getMfaType())
                .isVerified(security.getIsVerified())
                .build();
    }


    public void logout(String accountId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            SecurityInfo security = account.getSecurity();
            security.setTokenVersion(security.getTokenVersion() + 1); // làm cho token cũ vô hiệu
            account.setSecurity(security);
            accountRepository.save(account);
        });
    }

    private boolean isPasswordValid(String rawPassword, Account account) {
        var pass = passwordEncoder.encode(rawPassword);
        return passwordEncoder.matches(rawPassword, account.getPassword());
    }
}
