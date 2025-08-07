package doanh.io.graph_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PendingMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long groupId;

    private LocalDateTime createJoinAt;

    private LocalDateTime acceptJoinAt;

    private LocalDateTime expiryAt;

    private String status;

    private String inviterId;

    private String reasonJoinGroup;

    @Enumerated(EnumType.STRING)
    private InviteType inviteType;

}
