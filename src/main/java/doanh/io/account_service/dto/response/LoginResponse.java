package doanh.io.account_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String id;
    private Boolean isMfaEnabled;
    private String mfaType;
    private Boolean isVerified;
}
