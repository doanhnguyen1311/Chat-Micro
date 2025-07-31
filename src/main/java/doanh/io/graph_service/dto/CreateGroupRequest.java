package doanh.io.graph_service.dto;

import doanh.io.graph_service.entity.Group;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateGroupRequest {
    private Group group;
    private List<String> memberIds;
}
