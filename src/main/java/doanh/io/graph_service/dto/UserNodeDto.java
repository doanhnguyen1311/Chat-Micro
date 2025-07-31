package doanh.io.graph_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNodeDto {
    private String id;
    private String name;
    private String address;
}
