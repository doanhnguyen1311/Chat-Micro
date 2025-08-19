package doanh.io.notification_service.service;

import doanh.io.notification_service.dto.APIResponse;
import doanh.io.notification_service.entity.Notification;
import doanh.io.notification_service.entity.NotificationStatus;
import doanh.io.notification_service.entity.NotificationType;
import doanh.io.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification createNotification(String senderId, String receiverId, NotificationType type, String senderName) {
        String fullContent = senderName + type.getMessage();
        Notification notification = Notification.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .type(type)
                .content(fullContent)
                .status(NotificationStatus.UNREAD)
                .build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(String receiverId) {
        return notificationRepository.findByReceiverIdAndStatusOrderByCreatedAtDesc(
                receiverId, NotificationStatus.UNREAD
        );
    }

    public List<Notification> getAllNotifications(String receiverId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId);
    }

    public long countUnreadNotifications(String receiverId) {
        return notificationRepository.countByReceiverIdAndStatus(receiverId, NotificationStatus.UNREAD);
    }

    @Transactional
    public APIResponse<?> markAllAsRead(String receiverId) {
        int updated = notificationRepository.updateStatusByReceiverId(receiverId, NotificationStatus.READ);
        return APIResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Đã đánh dấu " + updated + " thông báo là đã đọc")
                .data(updated)
                .build();
    }

    public APIResponse<?> markAsRead(Long notificationId, String receiverId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    if (!notification.getReceiverId().equals(receiverId)) {
                        return APIResponse.builder()
                                .success(false)
                                .statusCode(403)
                                .message("Bạn không phải người nhận thông báo này")
                                .data(null)
                                .build();
                    }

                    notification.setStatus(NotificationStatus.READ);
                    notificationRepository.save(notification);

                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Đánh dấu đã đọc thành công")
                            .data(notification)
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy thông báo")
                        .data(null)
                        .build()
                );
    }

    public APIResponse<?> deleteNotification(Long notificationId, String receiverId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    if (!notification.getReceiverId().equals(receiverId)) {
                        return APIResponse.builder()
                                .success(false)
                                .statusCode(403)
                                .message("Bạn không có quyền xoá thông báo này")
                                .build();
                    }
                    notificationRepository.delete(notification);
                    return APIResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Xoá thông báo thành công")
                            .build();
                })
                .orElseGet(() -> APIResponse.builder()
                        .success(false)
                        .statusCode(404)
                        .message("Không tìm thấy thông báo")
                        .build()
                );
    }

    @Transactional
    public int deleteAllNotifications(String receiverId) {
        int deletedCount = notificationRepository.deleteByReceiverId(receiverId);
        return deletedCount;
    }

}
