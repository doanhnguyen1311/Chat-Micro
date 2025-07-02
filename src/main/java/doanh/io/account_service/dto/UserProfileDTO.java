package doanh.io.account_service.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private String fullName;
    private String avatarUrl;
    private String coverPhotoUrl;
    private String bio;
    private String gender;
    private LocalDate birthday;
    private String location;
    private String website;
}
