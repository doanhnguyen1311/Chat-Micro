package doanh.io.account_service.dto;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class SecurityInfo {
    private boolean isVerified;
    private boolean mfaEnabled;
    private String mfaType;
    private String mfaSecret;
    private int loginAttempts;
    private boolean isLocked;
    private LocalDateTime lockTime;
    private int tokenVersion;
}
