package doanh.io.account_service.entity.info;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderInfo {
    private String provider;     // LOCAL / GOOGLE / FACEBOOK
    private String providerId;   // ID từ Google, Facebook...
}
