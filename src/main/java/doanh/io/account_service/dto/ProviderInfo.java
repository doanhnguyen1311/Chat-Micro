package doanh.io.account_service.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProviderInfo {
    private String provider;     // LOCAL / GOOGLE / FACEBOOK
    private String providerId;   // ID từ Google, Facebook...
}
