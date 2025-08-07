package doanh.io.graph_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "channels")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                   // Tên nhóm

    private String avatarUrl;             // Ảnh đại diện nhóm

    private String description;           // Mô tả (chỉ với SOCIAL)

    @Enumerated(EnumType.STRING)
    private GroupType type;               // CHAT hoặc SOCIAL

    private String createdByUserId;       // ID người tạo nhóm

    private LocalDateTime createdAt;

    private Boolean isPrivate;            // Nhóm riêng tư hay công khai (chỉ cần cho CHAT)

    private Boolean isDeleted;            // Đánh dấu xóa mềm

    @Enumerated(EnumType.STRING)
    private GroupJoinPolicy joinPolicy;   // OPEN, REQUEST, INVITE (chỉ với SOCIAL)

    private String coverImageUrl;         // Ảnh bìa (chỉ với SOCIAL)
}
