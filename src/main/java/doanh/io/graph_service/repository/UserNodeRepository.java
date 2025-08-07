package doanh.io.graph_service.repository;

import doanh.io.graph_service.dto.MutualFriendResult;
import doanh.io.graph_service.node.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {

    // Tạo user node nếu chưa tồn tại
    @Transactional
    @Query("""
            OPTIONAL MATCH (u:User {id: $id})
            WITH u
            WHERE u IS NULL
            CREATE (n:User {id: $id, name: $name, address: $address, avatar: $image})
            RETURN COUNT(n)
            """)
    int createUser(String id, String name, String address, String image);

    // Cập nhật thông tin user
    @Transactional
    @Query("""
            MATCH (u:User {id: $id})
            SET u.name = $name, u.address = $address, u.avatar = $image
            RETURN COUNT(u)
            """)
    int updateUser(String id, String name, String address, String image);

    // Xóa user node theo id
    @Transactional
    @Query("MATCH (u:User {id: $id}) DETACH DELETE u RETURN COUNT(u)")
    int deleteUserById(String id);


    /*
     * Relationship: FRIEND, REQUESTED
     */

    // Gửi lời mời kết bạn
    @Transactional
    @Query("""
            MATCH (a:User {id: $fromUserId}), (b:User {id: $toUserId})
            MERGE (a)-[:REQUESTED]->(b)
            RETURN COUNT(*)
            """)
    int sendFriendRequest(String fromUserId, String toUserId);

    // Hủy lời mời kết bạn
    @Transactional
    @Query("""
            MATCH (a:User {id: $fromUserId})-[r:REQUESTED]->(b:User {id: $toUserId})
            DELETE r
            RETURN COUNT(r)
            """)
    int cancelFriendRequest(String fromUserId, String toUserId);

    // Xác nhận lời mời kết bạn
    @Transactional
    @Query("""
            MATCH (a:User {id: $fromUserId})-[r:REQUESTED]->(b:User {id: $toUserId})
            DELETE r
            MERGE (a)-[:FRIEND]->(b)
            MERGE (b)-[:FRIEND]->(a)
            RETURN COUNT(*)
            """)
    int acceptFriendRequest(String fromUserId, String toUserId);

    // Từ chối lời mời (xóa edge)
    @Transactional
    @Query("""
            MATCH (a:User {id: $fromUserId})-[r:REQUESTED]->(b:User {id: $toUserId})
            DELETE r
            RETURN COUNT(r)
            """)
    int rejectFriendRequest(String fromUserId, String toUserId);

    // Xóa quan hệ bạn bè 2 chiều
    @Transactional
    @Query("""
            MATCH (a:User {id: $userId1})-[r1:FRIEND]->(b:User {id: $userId2})
            MATCH (b)-[r2:FRIEND]->(a)
            DELETE r1, r2
            RETURN COUNT(r1) + COUNT(r2)
            """)
    int removeFriend(String userId1, String userId2);

    // Lấy danh sách bạn bè
    @Query("MATCH (:User {id: $userId})-[:FRIEND]->(friend:User) RETURN distinct friend")
    List<UserNode> findFriends(String userId);

    // Lời mời đã gửi
    @Query("MATCH (:User {id: $userId})-[:REQUESTED]->(sent:User) RETURN distinct sent")
    List<UserNode> findFriendRequestsSent(String userId);

    // Lời mời đã nhận
    @Query("MATCH (received:User)-[:REQUESTED]->(:User {id: $userId}) RETURN distinct received")
    List<UserNode> findFriendRequestsReceived(String userId);

    // Đã là bạn bè chưa?
    @Query("MATCH (:User {id: $userId1})-[:FRIEND]->(:User {id: $userId2}) RETURN COUNT(*) > 0")
    Boolean areFriends(String userId1, String userId2);

    // Đã gửi lời mời?
    @Query("MATCH (:User {id: $userId1})-[:REQUESTED]->(:User {id: $userId2}) RETURN COUNT(*) > 0")
    Boolean hasSentRequest(String userId1, String userId2);

    // Đã nhận lời mời?
    @Query("MATCH (:User {id: $userId2})-[:REQUESTED]->(:User {id: $userId1}) RETURN COUNT(*) > 0")
    Boolean hasReceivedRequest(String userId1, String userId2);

    // Chặn người khác
    @Query("""
            MATCH (a:User {id: $fromUserId}), (b:User {id: $toUserId})
            MERGE (a)-[:BLOCKED]->(b)
            RETURN COUNT(*)
            """)
    int blockUser(String fromUserId, String toUserId);

    // Bỏ chặn
    @Query("""
            MATCH (a:User {id: $fromUserId})-[r:BLOCKED]->(b:User {id: $toUserId})
            DELETE r
            RETURN COUNT(r)
            """)
    int unblockUser(String fromUserId, String toUserId);

    // Kiểm tra bạn đã chặn người kia chưa
    @Query("""
            MATCH (a:User {id: $fromId})-[:BLOCKED]->(b:User {id: $toId})
            RETURN COUNT(*) > 0
            """)
    Boolean isBlocked(String fromId, String toId);

    @Query("""
            MATCH (u:User {id: $userId})-[:FRIEND]-(friend:User)
            RETURN friend LIMIT 6
            """)
    List<UserNode> getFriends(String userId);

    @Query("""
                MATCH (self:User {id: $selfId})-[:FRIEND]-(mutual:User)-[:FRIEND]-(target:User {id: $targetId})
                RETURN DISTINCT mutual
            """)
    List<UserNode> findMutualFriends(String selfId, String targetId);


    @Query("""
                MATCH (self:User {id: $selfId})-[:FRIEND]-(mutual:User)-[:FRIEND]-(target:User {id: $targetId})
                RETURN COUNT(DISTINCT mutual)
            """)
    Long countMutualFriends(String selfId, String targetId);

    @Query("MATCH (a:User {id: $selfId})-[r:FRIEND]-(b:User {id: $targetId}) DELETE r RETURN COUNT(r)")
    Long deleteFriendship(String selfId, String targetId);

    // Lấy danh sách user trong một group
    @Query("MATCH (g:Group {id: $groupId})<-[:MEMBER_OF]-(u:User) RETURN u")
    List<UserNode> findUsersInGroup(@Param("groupId") Long groupId);

    @Query("MATCH (u:User {id: $userId})-[:FRIEND]->(f:User) WHERE NOT (f)-[:MEMBER_OF]->(:Group {id: $groupId}) RETURN f")
    List<UserNode> findFriendsNotInGroup(@Param("userId") String userId, @Param("groupId") Long groupId);



}
