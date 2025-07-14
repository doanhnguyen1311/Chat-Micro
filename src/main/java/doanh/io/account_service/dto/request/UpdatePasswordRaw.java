package doanh.io.account_service.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
public class UpdatePasswordRaw {
    private String oldPassword;
    private String newPassword;
    private String id;
}
