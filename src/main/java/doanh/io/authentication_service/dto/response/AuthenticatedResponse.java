package doanh.io.authentication_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedResponse {
    private boolean isAuthentication;
    private String token;
}
