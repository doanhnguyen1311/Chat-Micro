package doanh.io.account_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderInfoDTO {
    private String provider;     // LOCAL / GOOGLE / FACEBOOK
    private String providerId;   // ID từ Google, Facebook...
}

