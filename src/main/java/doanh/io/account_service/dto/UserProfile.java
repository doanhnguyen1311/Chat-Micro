package doanh.io.account_service.dto;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class UserProfile {
    private String fullName;
    private String avatarUrl;
    private String coverPhotoUrl;
    private String bio;
    private String gender;
    private LocalDate birthday;
    private String location;
    private String website;
}
