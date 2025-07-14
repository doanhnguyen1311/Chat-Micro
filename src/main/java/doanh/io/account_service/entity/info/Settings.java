package doanh.io.account_service.entity.info;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Settings {
    private String theme;     // dark, light
    private String language;  // vi, en, etc.
    private Boolean soundOn;
    private Boolean notificationsEnabled;
}
