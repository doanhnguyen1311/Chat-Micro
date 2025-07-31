package doanh.io.graph_service.node;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.Set;

@Node("User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNode {
    @Id
    private String id;

    private String name;

    private String address;

    private String avatar;
}
