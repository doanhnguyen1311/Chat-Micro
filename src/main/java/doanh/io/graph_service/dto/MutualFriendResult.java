package doanh.io.graph_service.dto;

import doanh.io.graph_service.node.UserNode;
import lombok.*;

import java.util.List;

public interface MutualFriendResult {
    List<UserNode> getMutualFriends();
    Long getMutualCount();
}

