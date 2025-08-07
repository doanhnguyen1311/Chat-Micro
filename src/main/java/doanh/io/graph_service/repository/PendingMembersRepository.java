package doanh.io.graph_service.repository;

import doanh.io.graph_service.entity.PendingMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingMembersRepository extends JpaRepository<PendingMembers, Long> {
    boolean existsByGroupIdAndUserId(Long groupId, String userId);

    void deleteByGroupIdAndUserId(Long groupId, String userId);

    void deleteByGroupId(Long groupId);

    List<PendingMembers> findAllByGroupId(Long groupId);
}
