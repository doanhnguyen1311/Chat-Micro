package doanh.io.account_service.service;

import doanh.io.account_service.dto.SettingsDTO;
import doanh.io.account_service.dto.APIResponse; // Import APIResponse
import doanh.io.account_service.dto.request.SettingsUpdateRequest;
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.mapper.AccountMapper;
import doanh.io.account_service.mapper.SettingsMapper;
import doanh.io.account_service.repository.AccountRepository;
// import jakarta.persistence.EntityNotFoundException; // Không cần thiết nữa
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final SettingsMapper settingsMapper;

    public APIResponse<?> getSettings(String accountId) {
        var accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            return APIResponse.builder()
                    .data(null)
                    .success(false)
                    .statusCode(404)
                    .message("Không tìm thấy tài khoản với ID: " + accountId)
                    .build();
        }

        Account account = accountOptional.get();
        SettingsDTO settingsDTO = settingsMapper.toDto(account.getSettings());

        return APIResponse.builder()
                .data(settingsDTO)
                .success(true)
                .statusCode(200)
                .message("Lấy cài đặt tài khoản thành công")
                .build();
    }

    @Transactional
    public APIResponse<?> updateSettings(String accountId, SettingsUpdateRequest request) {
        var accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            return APIResponse.builder()
                    .data(null)
                    .success(false)
                    .statusCode(404)
                    .message("Không tìm thấy tài khoản với ID: " + accountId)
                    .build();
        }

        Account account = accountOptional.get();
        var settings = account.getSettings();

        if (request.getTheme() != null) {
            settings.setTheme(request.getTheme());
        }
        if (request.getLanguage() != null) {
            settings.setLanguage(request.getLanguage());
        }
        if (request.getSoundOn() != null) {
            settings.setSoundOn(request.getSoundOn());
        }
        if (request.getNotificationsEnabled() != null) {
            settings.setNotificationsEnabled(request.getNotificationsEnabled());
        }

        account.setSettings(settings);
        Account updatedAccount = accountRepository.save(account); // Lưu lại tài khoản để đảm bảo thay đổi được phản ánh

        // Có thể map lại settings từ updatedAccount để đảm bảo tính nhất quán nếu có logic phức tạp hơn
        SettingsDTO updatedSettingsDTO = settingsMapper.toDto(updatedAccount.getSettings());

        return APIResponse.builder()
                .data(updatedSettingsDTO)
                .statusCode(200)
                .success(true)
                .message("Cập nhật cài đặt tài khoản thành công")
                .build();
    }
}