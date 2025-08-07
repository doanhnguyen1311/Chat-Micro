package doanh.io.graph_service.dto;

import doanh.io.graph_service.entity.InviteType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleJoinGroup {
    private Long groupId;
    private String reason;
    private InviteType inviteType;
    private String inviterId;
}
