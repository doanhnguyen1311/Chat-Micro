package doanh.io.graph_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "channel_members")
public class GroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;         // ID của nhóm

    private String userId;        // ID của người dùng (tách sang User Service)

    @Enumerated(EnumType.STRING)
    private MemberRole role;      // ADMIN, MEMBER, MOD (nếu là social group)

    private LocalDateTime joinedAt;

    private boolean isBanned;     // Dùng cho group cộng đồng

    private boolean isMuted;      // Dùng cho group chat (nếu bạn có chức năng mute member)
}
