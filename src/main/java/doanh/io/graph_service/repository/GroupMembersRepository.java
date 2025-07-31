package doanh.io.graph_service.repository;

import doanh.io.graph_service.entity.GroupMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {
    // Có thể thêm các phương thức truy vấn tuỳ ý
}
