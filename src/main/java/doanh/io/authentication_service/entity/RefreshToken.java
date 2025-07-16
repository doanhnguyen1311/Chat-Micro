package doanh.io.authentication_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String token;
    private String userId;
    private String deviceId;
    private Long tokenVersion;
    private LocalDateTime expiresAt; // Thời điểm token hết hạn

    private LocalDateTime createdAt;

    private Boolean isDefaultDevice;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
