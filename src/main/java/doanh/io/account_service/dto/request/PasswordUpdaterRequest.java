package doanh.io.account_service.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordUpdaterRequest {
    private String oldPassword;
    private String newPassword;
}
