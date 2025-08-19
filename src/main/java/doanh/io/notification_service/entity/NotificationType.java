package doanh.io.notification_service.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    // Kết bạn
    FRIEND_REQUEST(" đã gửi cho bạn lời mời kết bạn!"),
    FRIEND_ACCEPT(" đã chấp nhận lời mời kết bạn!"),
    FRIEND_REJECT(" đã từ chối lời mời kết bạn."),

    // Bài viết / tương tác
    POST_LIKE(" đã thích bài viết của bạn."),
    POST_COMMENT(" đã bình luận về bài viết của bạn."),
    POST_SHARE(" đã chia sẻ bài viết của bạn."),
    POST_TAG(" đã gắn thẻ bạn trong một bài viết."),

    // Tin nhắn
    MESSAGE(" đã gửi cho bạn một tin nhắn mới."),

    // Nhóm
    GROUP_INVITE(" đã mời bạn tham gia nhóm."),
    GROUP_JOIN_REQUEST(" đã yêu cầu tham gia nhóm của bạn."),
    GROUP_APPROVAL(" đã chấp nhận yêu cầu tham gia nhóm của bạn."),
    GROUP_REJECTION(" đã từ chối yêu cầu tham gia nhóm của bạn."),

    // Sự kiện
    EVENT_INVITE(" đã mời bạn tham gia sự kiện."),
    EVENT_REMINDER(" Sự kiện sắp diễn ra."),

    // Hệ thống
    SYSTEM(" có một thông báo từ hệ thống."),
    SECURITY(" Cảnh báo bảo mật tài khoản của bạn.");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }
}
