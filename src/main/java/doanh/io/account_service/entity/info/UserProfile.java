package doanh.io.account_service.entity.info;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
