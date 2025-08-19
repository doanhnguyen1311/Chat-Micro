package doanh.io.notification_service.controller;

import doanh.io.notification_service.dto.APIResponse;
import doanh.io.notification_service.dto.NotificationDTO;
import doanh.io.notification_service.entity.Notification;
import doanh.io.notification_service.entity.NotificationType;
import doanh.io.notification_service.service.AuthenticationService;
import doanh.io.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    private String getUserIdFromCookie(String userIdCookie) {
        return authenticationService.decryptUserId(userIdCookie);
    }


    @PostMapping
    public APIResponse<?> sendNotification(
            @RequestBody NotificationDTO payload
            ) {
        try {
            log.info("Sending notification: {}", payload);
            Notification notification = notificationService.createNotification(payload.getSenderId(), payload.getReceiverId(), payload.getType(), payload.getSenderName());
            return APIResponse.builder()
                    .success(true)
                    .message("Notification created successfully")
                    .data(notification)
                    .statusCode(201)
                    .build();
        } catch (Exception e) {
            return APIResponse.builder()
                    .success(false)
                    .message("Failed to create notification: " + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .build();
        }
    }

    @GetMapping("/unread")
    public APIResponse<?> getUnreadNotifications(@CookieValue(value = "userId", required = false) String userId) {
        try {
            userId = authenticationService.decryptUserId(userId);

            if (userId == null) {
                return APIResponse.builder()
                        .success(false)
                        .message("Unauthorized")
                        .data(null)
                        .statusCode(403)
                        .build();
            }

            List<Notification> notifications = notificationService.getUnreadNotifications(userId);
            return APIResponse.builder()
                    .success(true)
                    .message("Lấy danh sách thông báo chưa đọc thành công!")
                    .data(notifications)
                    .statusCode(200)
                    .build();
        } catch (Exception e) {
            return APIResponse.builder()
                    .success(false)
                    .message("Failed to fetch unread notifications: " + e.getMessage())
                    .data(null)
                    .statusCode(500)
                    .build();
        }
    }

    @PutMapping("/{notificationId}/read")
    public APIResponse<?> markAsRead(
            @PathVariable Long notificationId,
            @CookieValue(value = "userId", required = false) String userId
    ) {
        userId = authenticationService.decryptUserId(userId);
        if (userId == null) {
            return APIResponse.builder()
                    .success(false)
                    .statusCode(401)
                    .message("Unauthorized")
                    .build();
        }

        return notificationService.markAsRead(notificationId, userId);
    }

    @GetMapping
    public List<Notification> getAll(@CookieValue(value = "userId", required = false) String userId) {
        String userIdDecrypted = getUserIdFromCookie(userId);
        return notificationService.getAllNotifications(userIdDecrypted);
    }

    @GetMapping("/unread/count")
    public long countUnread(@CookieValue(value = "userId", required = false) String userId) {
        String userIdDecrypted = getUserIdFromCookie(userId);
        return notificationService.countUnreadNotifications(userIdDecrypted);
    }

    @PutMapping("/read-all")
    public APIResponse<?> markAllAsRead(
            @CookieValue(value = "userId", required = false) String userId
    ) {
        String userIdDecrypted = getUserIdFromCookie(userId);
        return notificationService.markAllAsRead(userIdDecrypted);
    }

    @DeleteMapping("/{notificationId}")
    public APIResponse<?> delete(
            @PathVariable Long notificationId,
            @CookieValue(value = "userId", required = false) String userId
    ) {
        String userIdDecrypted = getUserIdFromCookie(userId);
        return notificationService.deleteNotification(notificationId, userIdDecrypted);
    }

    @DeleteMapping
    public APIResponse<?> deleteAll(@CookieValue(value = "userId", required = false) String userId) {
        String userIdDecrypted = getUserIdFromCookie(userId);
        var count = notificationService.deleteAllNotifications(userIdDecrypted);
        return APIResponse.builder().data(count)
                .success(true)
                .message(count > 0 ? "Xóa thành công " + count + " thông báo!" : "Không có thông báo nào được xóa!")
                .build();
    }



}
