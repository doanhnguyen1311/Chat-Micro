package doanh.io.graph_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutualFriendDto {
    private String id;
    private String name;
    private String address;
    private String avatar;
}