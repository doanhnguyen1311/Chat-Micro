package doanh.io.graph_service.repository;

import doanh.io.graph_service.node.GroupNode;
import doanh.io.graph_service.node.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupNodeRepository extends Neo4jRepository<GroupNode, Long> {

    @Query("""
            MERGE (g:Group {id: $id})
            SET g.name = $name,
                g.type = $type,
                g.avatarUrl = $avatarUrl,
                g.isPrivate = $isPrivate
            RETURN g
            """)
    GroupNode createOrUpdateGroupNode(Long id, String name, String type, String avatarUrl, Boolean isPrivate);


    // Tìm nhóm theo tên
    Optional<GroupNode> findByName(String name);

    // Tìm tất cả các nhóm theo loại (CHAT hoặc SOCIAL)
    List<GroupNode> findByType(String type);

    // Tìm nhóm theo tên và loại
    Optional<GroupNode> findByNameAndType(String name, String type);

    // Tìm tất cả các nhóm mà một user tham gia
    @Query("MATCH (g:Group)<-[:MEMBER_OF]-(u:User {id: $userId}) WHERE g.isDeleted = false RETURN g")
    List<Object> findGroupsByUserId(String userId);

    // Tìm tất cả các nhóm mà một user là admin
    @Query("MATCH (g:Group)<-[:ADMIN_OF]-(u:User {id: $userId}) WHERE g.isDeleted = false RETURN g")
    List<Object> findGroupsByAdminId(String userId);

    // Tìm tất cả các user trong một nhóm
    @Query("MATCH (g:Group {id: $groupId})<-[:MEMBER_OF]-(u:User) RETURN u.id as id, u.name as name, u.address as address, u.avatar as avatar")
    List<UserNode> findUsersInGroup(Long groupId);

    // Đếm số lượng thành viên trong một nhóm
    @Query("MATCH (g:Group {id: $groupId})<-[:MEMBER_OF]-(u:User) RETURN count(u)")
    Long countMembersInGroup(Long groupId);

    // Tìm các nhóm có avatarUrl không null
    List<GroupNode> findByAvatarUrlIsNotNull();

    // Xóa một nhóm và tất cả các mối quan hệ liên quan
    @Query("MATCH (g:Group {id: $groupId}) DETACH DELETE g")
    void deleteGroupAndRelationships(Long groupId);

    @Query("""
        MATCH (u:User {id: $userId}), (g:Group {id: $groupId})
        MERGE (u)-[:MEMBER_OF]->(g)
        RETURN g
        """)
    void addUserToGroup(String userId, Long groupId);

    @Query("""
        MATCH (u:User {id: $userId}), (g:Group {id: $groupId})
        MERGE (u)-[:MEMBER_OF]->(g)
        RETURN count(g) > 0
        """)
    boolean addUserToGroupPolicy(String userId, Long groupId);

    @Query("""
        MATCH (u:User {id: $userId})-[r:MEMBER_OF]->(g:Group {id: $groupId})
        DELETE r
        RETURN COUNT(r) > 0
    """)
    boolean leaveGroup(Long groupId, String userId);
}