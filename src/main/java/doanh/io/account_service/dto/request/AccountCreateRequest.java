package doanh.io.account_service.dto.request;

import doanh.io.account_service.dto.ProviderInfoDTO;
import doanh.io.account_service.dto.SettingsDTO;
import doanh.io.account_service.dto.UserProfileDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequest {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    private UserProfileDTO profile;

    private ProviderInfoDTO provider;

    private SettingsDTO settings;
}
