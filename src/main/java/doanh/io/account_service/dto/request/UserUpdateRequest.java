package doanh.io.account_service.dto.request;

import doanh.io.account_service.dto.UserProfileDTO;
import doanh.io.account_service.dto.ProviderInfoDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    private String username;
    private String email;
    private String phoneNumber;

    private UserProfileDTO profile;

    private ProviderInfoDTO provider;
}
