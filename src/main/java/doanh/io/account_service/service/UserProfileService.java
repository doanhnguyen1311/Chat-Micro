package doanh.io.account_service.service;

import doanh.io.account_service.dto.UserProfileDTO;
import doanh.io.account_service.dto.APIResponse; // Import APIResponse
import doanh.io.account_service.entity.Account;
import doanh.io.account_service.mapper.AccountMapper;
import doanh.io.account_service.mapper.UserProfileMapper;
import doanh.io.account_service.repository.AccountRepository;
// import jakarta.persistence.EntityNotFoundException; // Không còn cần thiết vì chúng ta sẽ trả về APIResponse
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper; // Giữ lại nếu có thể dùng sau
    private final UserProfileMapper userProfileMapper;

    @Transactional(readOnly = true)
    public APIResponse<?> getProfile(String accountId) {
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
        UserProfileDTO userProfileDTO = userProfileMapper.toDTO(account.getProfile());

        return APIResponse.builder()
                .data(userProfileDTO)
                .success(true)
                .statusCode(200)
                .message("Lấy thông tin profile thành công")
                .build();
    }

    @Transactional
    public APIResponse<?> updateProfile(String accountId, UserProfileDTO dto) {
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
        var profile = account.getProfile(); // Lấy đối tượng profile hiện có

        // Cập nhật các trường profile nếu chúng không null trong DTO đầu vào
        if (dto.getFullName() != null) profile.setFullName(dto.getFullName());
        if (dto.getAvatarUrl() != null) profile.setAvatarUrl(dto.getAvatarUrl());
        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getBirthday() != null) profile.setBirthday(dto.getBirthday());
        if (dto.getGender() != null) profile.setGender(dto.getGender());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
        if (dto.getWebsite() != null) profile.setWebsite(dto.getWebsite());
        if (dto.getCoverPhotoUrl() != null) profile.setCoverPhotoUrl(dto.getCoverPhotoUrl());

        // Lưu tài khoản đã cập nhật. Vì profile là một phần của account, việc này sẽ lưu luôn profile.
        Account updatedAccount = accountRepository.save(account);

        // Chuyển đổi profile đã cập nhật thành DTO để trả về
        UserProfileDTO updatedProfileDTO = userProfileMapper.toDTO(updatedAccount.getProfile());

        return APIResponse.builder()
                .data(updatedProfileDTO)
                .statusCode(200)
                .success(true)
                .message("Cập nhật thông tin profile thành công")
                .build();
    }
}