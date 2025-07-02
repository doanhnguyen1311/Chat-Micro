package doanh.io.account_service.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityInfoDTO {
    private boolean isVerified;
    private boolean mfaEnabled;
    private String mfaType;
    private String mfaSecret;
    private int loginAttempts;
    private boolean isLocked;
    private LocalDateTime lockTime;
    private int tokenVersion;
}