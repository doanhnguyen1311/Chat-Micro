package doanh.io.account_service.dto.request;

import doanh.io.account_service.dto.ProviderInfoDTO;
import doanh.io.account_service.dto.UserProfileDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    // Tuỳ chọn (nếu form đăng ký nâng cao)
    private UserProfileDTO profile;

    // Tuỳ chọn (nếu đăng nhập xã hội)
    private ProviderInfoDTO provider;
}
