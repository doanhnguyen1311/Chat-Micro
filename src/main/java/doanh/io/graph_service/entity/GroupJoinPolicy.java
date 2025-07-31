package doanh.io.graph_service.entity;

public enum GroupJoinPolicy {
    OPEN,        // Ai cũng vào được
    REQUEST,     // Gửi yêu cầu chờ duyệt
    INVITE       // Chỉ ai được mời mới vào được
}
