package doanh.io.account_service.entity.info;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityInfo {
    private Boolean isVerified;
    private Boolean mfaEnabled;
    private String mfaType;
    private String mfaSecret;
    private int loginAttempts;
    private Boolean isLocked;
    private LocalDateTime lockTime;
    private int tokenVersion;
}
