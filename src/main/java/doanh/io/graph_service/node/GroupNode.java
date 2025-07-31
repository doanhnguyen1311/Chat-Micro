package doanh.io.graph_service.node;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupNode {

    @Id
    private Long id; // trùng với GroupId

    private String name;

    private String type; // CHAT hoặc SOCIAL

    private String avatarUrl;
}
