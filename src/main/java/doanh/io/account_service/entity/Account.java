package doanh.io.account_service.entity;

import doanh.io.account_service.dto.ProviderInfo;
import doanh.io.account_service.dto.SecurityInfo;
import doanh.io.account_service.dto.Settings;
import doanh.io.account_service.dto.UserProfile;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
