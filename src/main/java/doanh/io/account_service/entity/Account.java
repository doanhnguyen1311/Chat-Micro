package doanh.io.account_service.entity;

import doanh.io.account_service.entity.info.ProviderInfo;
import doanh.io.account_service.entity.info.SecurityInfo;
import doanh.io.account_service.entity.info.Settings;
import doanh.io.account_service.entity.info.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_created_at", columnList = "createdAt"),
                @Index(name = "idx_phone_number", columnList = "phoneNumber")
        }
)
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    private Boolean isOnline;
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
