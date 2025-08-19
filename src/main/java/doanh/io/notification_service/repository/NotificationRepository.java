package doanh.io.notification_service.repository;

import doanh.io.notification_service.entity.Notification;
import doanh.io.notification_service.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(String receiverId);

    List<Notification> findByReceiverIdAndStatusOrderByCreatedAtDesc(String receiverId, NotificationStatus status);

    long countByReceiverIdAndStatus(String receiverId, NotificationStatus status);

    @Modifying
    @Query("UPDATE Notification n SET n.status = :status WHERE n.receiverId = :receiverId")
    int updateStatusByReceiverId(String receiverId, NotificationStatus status);

    int deleteByReceiverId(String receiverId);
}
