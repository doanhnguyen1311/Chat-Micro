package doanh.io.account_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingsDTO {
    private String theme;     // dark, light
    private String language;  // vi, en, etc.
    private boolean soundOn;
    private boolean notificationsEnabled;
}
