package doanh.io.notification_service.dto;

import doanh.io.notification_service.entity.NotificationStatus;
import doanh.io.notification_service.entity.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private NotificationType type;
    private String content;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
