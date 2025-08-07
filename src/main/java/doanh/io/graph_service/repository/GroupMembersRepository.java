package doanh.io.graph_service.repository;

import doanh.io.graph_service.entity.GroupMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {
    boolean existsByGroupIdAndUserId(Long groupId, String friendId);

    List<GroupMembers> findByGroupId(Long groupId);

    int deleteByGroupIdAndUserId(Long groupId, String userId);

    GroupMembers findByUserIdAndGroupId(String userId, Long groupId);
    // Có thể thêm các phương thức truy vấn tuỳ ý
}
