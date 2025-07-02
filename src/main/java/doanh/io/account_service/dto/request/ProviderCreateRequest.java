package doanh.io.account_service.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderCreateRequest {

    private String provider;     // GOOGLE, FACEBOOK, APPLE, LOCAL...
    private String providerId;   // ID thực tế lấy từ Google/Facebook API
}
