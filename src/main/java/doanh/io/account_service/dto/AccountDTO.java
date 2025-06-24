package doanh.io.account_service.dto;
import jakarta.persistence.Embedded;
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

    @Embedded
    private UserProfile profile;

    @Embedded
    private SecurityInfo security;

    @Embedded
    private ProviderInfo provider;

    @Embedded
    private Settings settings;
}
