package doanh.io.account_service.dto;
import doanh.io.account_service.entity.info.ProviderInfo;
import doanh.io.account_service.entity.info.Settings;
import doanh.io.account_service.entity.info.UserProfile;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    private String id;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    private boolean isOnline;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    private String status; // ACTIVE / SUSPENDED...

    private UserProfileDTO profile;

    private ProviderInfoDTO provider;

    private SettingsDTO settings;
}
