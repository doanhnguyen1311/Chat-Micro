package doanh.io.account_service.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsUpdateRequest {

    private String theme;        // "light", "dark"
    private String language;     // "vi", "en", etc.
    private Boolean soundOn;
    private Boolean notificationsEnabled;
}
