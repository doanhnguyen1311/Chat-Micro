package doanh.io.graph_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRelationDto {
    private String fromUserId;
    private String toUserId;
    private String status;
}
