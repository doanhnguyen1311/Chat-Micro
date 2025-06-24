package doanh.io.account_service.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public class Settings {
    private String theme;     // dark, light
    private String language;  // vi, en, etc.
    private boolean soundOn;
    private boolean notificationsEnabled;
}
