package doanh.io.graph_service.service;

import doanh.io.graph_service.dto.SimpleJoinGroup;
import doanh.io.graph_service.entity.*;
import doanh.io.graph_service.node.GroupNode;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.repository.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository; // JPA Repository for MySQL

    @Autowired
    private GroupNodeRepository groupNodeRepository; // Neo4j Repository

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private UserNodeRepository userNodeRepository;
    @Autowired
    private PendingMembersRepository pendingMembersRepository;

    // Tạo mới một nhóm
    @Transactional
    public Group createGroup(Group group) {
        // Đặt thời gian tạo và trạng thái mặc định
        group.setCreatedAt(LocalDateTime.now());
        group.setIsDeleted(false);

        // Lưu vào MySQL
        Group savedGroup = groupRepository.save(group);

        // Thêm người tạo vào bảng GroupMembers với role ADMIN
        GroupMembers creatorMember = new GroupMembers();
        creatorMember.setGroupId(savedGroup.getId());
        creatorMember.setUserId(savedGroup.getCreatedByUserId());
        creatorMember.setRole(MemberRole.ADMIN);
        creatorMember.setJoinedAt(LocalDateTime.now());
        creatorMember.setBanned(false);
        creatorMember.setMuted(false);
        groupMembersRepository.save(creatorMember);

        groupNodeRepository.createOrUpdateGroupNode(savedGroup.getId(), savedGroup.getName(), savedGroup.getType().toString(), savedGroup.getAvatarUrl(), savedGroup.getIsPrivate());

        // Thêm quan hệ MEMBER_OF giữa người tạo và group trong Neo4j
        groupNodeRepository.addUserToGroup(savedGroup.getCreatedByUserId(), savedGroup.getId());

        return savedGroup;
    }

    // Cập nhật thông tin nhóm
    @Transactional
    public Group updateGroup(Long id, Group updatedGroup) {
        // Tìm nhóm trong MySQL
        Optional<Group> existingGroup = groupRepository.findById(id);
        if (!existingGroup.isPresent()) {
            throw new RuntimeException("Group not found with id: " + id);
        }
        Group group = existingGroup.get();
        // Cập nhật các trường cho phép
        if (updatedGroup.getName() != null) {
            group.setName(updatedGroup.getName());
        }
        if (updatedGroup.getAvatarUrl() != null) {
            group.setAvatarUrl(updatedGroup.getAvatarUrl());
        }
        if (updatedGroup.getDescription() != null && group.getType() == GroupType.SOCIAL) {
            group.setDescription(updatedGroup.getDescription());
        }
        if (updatedGroup.getType() != null) {
            group.setType(updatedGroup.getType());
        }
        if (updatedGroup.getCreatedByUserId() != null) {
            group.setCreatedByUserId(updatedGroup.getCreatedByUserId());
        }
        if (updatedGroup.getJoinPolicy() != null && group.getType() == GroupType.SOCIAL) {
            group.setJoinPolicy(updatedGroup.getJoinPolicy());
        }
        if (updatedGroup.getCoverImageUrl() != null && group.getType() == GroupType.SOCIAL) {
            group.setCoverImageUrl(updatedGroup.getCoverImageUrl());
        }
        group.setIsPrivate(updatedGroup.getIsPrivate());
        // Lưu vào MySQL
        Group savedGroup = groupRepository.save(group);
        // Lấy node hiện tại từ Neo4j
        GroupNode currentNode = groupNodeRepository.findById(id).orElse(null);
        String name = savedGroup.getName();
        String type = savedGroup.getType().name();
        String avatarUrl = savedGroup.getAvatarUrl();
        if (currentNode != null) {
            if (name == null) name = currentNode.getName();
            if (type == null) type = currentNode.getType();
            if (avatarUrl == null) avatarUrl = currentNode.getAvatarUrl();
        }
        // Cập nhật trong Neo4j
        groupNodeRepository.createOrUpdateGroupNode(id, name, type, avatarUrl, savedGroup.getIsPrivate());
        return savedGroup;
    }

    // Xóa mềm một nhóm
    @Transactional
    public void softDeleteGroup(Long id) {
        // Tìm nhóm trong MySQL
        Optional<Group> existingGroup = groupRepository.findById(id);
        if (!existingGroup.isPresent()) {
            throw new RuntimeException("Group not found with id: " + id);
        }

        Group group = existingGroup.get();
        group.setIsDeleted(true);

        // Lưu vào MySQL
        groupRepository.save(group);

        // Xóa node và các mối quan hệ trong Neo4j
        groupNodeRepository.deleteGroupAndRelationships(id);
    }

    // Lấy nhóm theo ID
    public Optional<Group> getGroupById(Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isPresent() && Boolean.TRUE.equals(groupOpt.get().getIsDeleted())) {
            return Optional.empty();
        }
        return groupOpt;
    }

    // Lấy danh sách nhóm theo loại
    public List<Group> getGroupsByType(GroupType type) {
        return groupRepository.findByTypeAndIsDeleted(type, false);
    }

    // Lấy danh sách nhóm mà một user tham gia (từ Neo4j)
    public List<Object> getGroupsByUserId(String userId) {
        return groupNodeRepository.findGroupsByUserId(userId);
    }

    // Lấy danh sách user trong một nhóm (từ Neo4j)
    public List<UserNode> getUsersInGroup(Long groupId) {
        return userNodeRepository.findUsersInGroup(groupId);
    }

    public List<GroupMembers> getGroupMembers(Long groupId) {
        return groupMembersRepository.findByGroupId(groupId);
    }

    // Đếm số lượng thành viên trong một nhóm (từ Neo4j)
    public Long countMembersInGroup(Long groupId) {
        return groupNodeRepository.countMembersInGroup(groupId);
    }

    // Chuyển đổi từ Group (MySQL) sang GroupNode (Neo4j)
    private GroupNode convertToGroupNode(Group group) {
        GroupNode groupNode = new GroupNode();
        groupNode.setId(group.getId());
        groupNode.setName(group.getName());
        groupNode.setType(group.getType().name());
        groupNode.setAvatarUrl(group.getAvatarUrl());
        return groupNode;
    }

    /**
     * Tạo nhóm mới và thêm danh sách thành viên ban đầu
     *
     * @param group     thông tin nhóm
     * @param memberIds danh sách userId thành viên (ngoài người tạo)
     * @return nhóm đã tạo
     */
    @Transactional
    public Group createGroupWithMembers(Group group, List<String> memberIds) {
        group.setCreatedAt(LocalDateTime.now());
        group.setIsDeleted(false);
        Group savedGroup = groupRepository.save(group);

        // Thêm người tạo vào bảng GroupMembers với role ADMIN
        GroupMembers creatorMember = new GroupMembers();
        creatorMember.setGroupId(savedGroup.getId());
        creatorMember.setUserId(savedGroup.getCreatedByUserId());
        creatorMember.setRole(MemberRole.ADMIN);
        creatorMember.setJoinedAt(LocalDateTime.now());
        creatorMember.setBanned(false);
        creatorMember.setMuted(false);
        groupMembersRepository.save(creatorMember);
        groupNodeRepository.createOrUpdateGroupNode(savedGroup.getId(), savedGroup.getName(), savedGroup.getType().toString(), savedGroup.getAvatarUrl(), savedGroup.getIsPrivate());
        groupNodeRepository.addUserToGroup(savedGroup.getCreatedByUserId(), savedGroup.getId());

        // Thêm các thành viên khác
        if (memberIds != null) {
            for (String memberId : memberIds) {
                if (memberId == null || memberId.equals(savedGroup.getCreatedByUserId())) continue;
                GroupMembers member = new GroupMembers();
                member.setGroupId(savedGroup.getId());
                member.setUserId(memberId);
                member.setRole(MemberRole.MEMBER);
                member.setJoinedAt(LocalDateTime.now());
                member.setBanned(false);
                member.setMuted(false);
                groupMembersRepository.save(member);
                groupNodeRepository.addUserToGroup(memberId, savedGroup.getId());
            }
        }
        return savedGroup;
    }

    // Kiểm tra quyền thành viên
    public boolean isAdmin(Long groupId, String userId) {
        return groupMembersRepository.findAll().stream()
                .anyMatch(m -> m.getGroupId().equals(groupId) && m.getUserId().equals(userId) && m.getRole().name().equals("ADMIN"));
    }

    // Kiểm tra thành viên nhóm
    public boolean isMember(Long groupId, String userId) {
        return groupMembersRepository.findAll().stream()
                .anyMatch(m -> m.getGroupId().equals(groupId) && m.getUserId().equals(userId));
    }

    // Lấy danh sách bạn bè chưa có trong nhóm
    public List<UserNode> getFriendsNotInGroup(String userId, Long groupId) {
        return userNodeRepository.findFriendsNotInGroup(userId, groupId);
    }

    @Transactional
    public void addFriendsToGroup(Long groupId, List<String> friendIds, String userId) {
        Optional<Group> groupOpt = getGroupById(groupId);
        if (groupOpt.isEmpty()) throw new RuntimeException("Group not found");
        Group group = groupOpt.get();
        boolean isAdmin = isAdmin(groupId, userId);
        GroupJoinPolicy joinPolicy = group.getJoinPolicy();
        if (isAdmin) {
            // Admin thêm trực tiếp
            for (String friendId : friendIds) {
                if (!groupMembersRepository.existsByGroupIdAndUserId(groupId, friendId)) {
                    GroupMembers member = new GroupMembers();
                    member.setGroupId(groupId);
                    member.setUserId(friendId);
                    member.setRole(MemberRole.MEMBER);
                    member.setJoinedAt(LocalDateTime.now());
                    member.setBanned(false);
                    member.setMuted(false);
                    groupMembersRepository.save(member);
                }
                groupNodeRepository.addUserToGroup(friendId, groupId);
            }
        } else {
            if (joinPolicy == GroupJoinPolicy.OPEN) {
                // Thêm trực tiếp
                for (String friendId : friendIds) {
                    if (!groupMembersRepository.existsByGroupIdAndUserId(groupId, friendId)) {
                        GroupMembers member = new GroupMembers();
                        member.setGroupId(groupId);
                        member.setUserId(friendId);
                        member.setRole(MemberRole.MEMBER);
                        member.setJoinedAt(LocalDateTime.now());
                        member.setBanned(false);
                        member.setMuted(false);
                        groupMembersRepository.save(member);
                    }
                    groupNodeRepository.addUserToGroup(friendId, groupId);
                }
            } else if (joinPolicy == GroupJoinPolicy.REQUEST || joinPolicy == GroupJoinPolicy.INVITE) {
                // TODO: Gửi request chờ admin/kiểm duyệt duyệt (chưa implement)
                // Có thể lưu vào bảng request hoặc gửi thông báo cho admin
                throw new RuntimeException("Yêu cầu đang chờ kiểm duyệt bởi admin hoặc kiểm duyệt viên");
            }
        }
    }

    @Transactional
    public boolean changeModeratorRole(Long groupId, String userId, MemberRole targetRole) {
        GroupMembers member = groupMembersRepository.findAll().stream()
                .filter(m -> m.getGroupId().equals(groupId) && m.getUserId().equals(userId))
                .findFirst().orElse(null);
        if (member == null) return false;
        member.setRole(targetRole);
        groupMembersRepository.save(member);
        return true;
    }

    // xoa mot nguoi khoi nhom

    @Transactional
    public boolean removeUserFromGroup(Long groupId, String userId) {
        groupMembersRepository.deleteByGroupIdAndUserId(groupId, userId);
        groupNodeRepository.leaveGroup(groupId, userId);
        return true;
    }


    // join group

    @Transactional
    public boolean joinOpenGroup(Long groupId, String userId) {
        GroupMembers member = GroupMembers.builder()
                .groupId(groupId)
                .joinedAt(LocalDateTime.now())
                .userId(userId)
                .role(MemberRole.MEMBER)
                .build();

        var resSQL = groupMembersRepository.save(member);
        var resNeo = groupNodeRepository.addUserToGroupPolicy(userId, groupId);

        return (resNeo && resSQL.getId() != null);
    }

    @Transactional
    public boolean joinRequestGroup(Long groupId, String userId, InviteType inviteType, String reason, String inviterId) {
        PendingMembers pending = PendingMembers.builder()
                .userId(userId)
                .groupId(groupId)
                .createJoinAt(LocalDateTime.now())
                .expiryAt(LocalDateTime.now().plusHours(24))
                .status("PENDING")
                .inviteType(inviteType)
                .inviterId(inviterId)
                .reasonJoinGroup(reason)
                .build();
        var pendingRes = pendingMembersRepository.save(pending);

        return pendingRes.getId() != null;
    }

    public boolean isPending(Long groupId, String userId) {
        return pendingMembersRepository.existsByGroupIdAndUserId(groupId, userId);
    }


    @Transactional
    public boolean processInviteLink(SimpleJoinGroup simpleJoinGroup, String userId) {
        String inviterId = simpleJoinGroup.getInviterId();
        Long groupId = simpleJoinGroup.getGroupId();

        var groupOpt = groupRepository.findById(groupId);

        var group = groupOpt.get();

        if (group.getJoinPolicy() == GroupJoinPolicy.OPEN) {
            if (!isMember(groupId, userId)) {
                GroupMembers member = GroupMembers.builder()
                        .groupId(groupId)
                        .userId(userId)
                        .role(MemberRole.MEMBER)
                        .joinedAt(LocalDateTime.now())
                        .build();

                groupMembersRepository.save(member);
                groupNodeRepository.addUserToGroup(userId, groupId);
            }
            return true;
        }

        // Nếu là admin → thêm thẳng vào nhóm
        if (isAdmin(groupId, inviterId)) {
            if (!isMember(groupId, userId)) {
                GroupMembers member = GroupMembers.builder()
                        .groupId(groupId)
                        .userId(userId)
                        .role(MemberRole.MEMBER)
                        .joinedAt(LocalDateTime.now())
                        .build();

                groupMembersRepository.save(member);
                groupNodeRepository.addUserToGroup(userId, groupId);
            }
            return true;
        }

        // Nếu đã là member thì bỏ qua
        if (isMember(groupId, userId)) {
            return false;
        }

        // Nếu chưa pending → lưu request join
        if (!isPending(groupId, userId)) {
            return joinRequestGroup(
                    groupId,
                    userId,
                    simpleJoinGroup.getInviteType(),
                    simpleJoinGroup.getReason(),
                    simpleJoinGroup.getInviterId()
            );
        }

        return false;
    }

    @Transactional
    public boolean acceptRequestJoinGroup(Long groupId, String userId) {
        if (!isMember(groupId, userId)) {
            pendingMembersRepository.deleteByGroupIdAndUserId(groupId, userId);
            GroupMembers member = GroupMembers.builder()
                    .groupId(groupId)
                    .userId(userId)
                    .role(MemberRole.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();

            groupMembersRepository.save(member);
            groupNodeRepository.addUserToGroup(userId, groupId);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean rejectRequestJoinGroup(Long groupId, String userId) {
        if (!isMember(groupId, userId)) {
            pendingMembersRepository.deleteByGroupIdAndUserId(groupId, userId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean acceptAllRequestJoinGroup(Long groupId) {
        var pendingList = pendingMembersRepository.findAllByGroupId(groupId);

        if (pendingList.isEmpty()) return false;

        for (PendingMembers pending : pendingList) {
            String userId = pending.getUserId();

            GroupMembers member = GroupMembers.builder()
                    .groupId(groupId)
                    .userId(userId)
                    .role(MemberRole.MEMBER)
                    .joinedAt(LocalDateTime.now())
                    .build();

            groupMembersRepository.save(member);
            groupNodeRepository.addUserToGroup(userId, groupId);
        }

        pendingMembersRepository.deleteByGroupId(groupId); // xóa sau

        return true;
    }

    @Transactional
    public boolean rejectAllRequestJoinGroup(Long groupId) {
        pendingMembersRepository.deleteByGroupId(groupId);
        return true;
    }

    public List<PendingMembers> getAllPendingMembersByGroupId(Long groupId) {
        List<PendingMembers> list = pendingMembersRepository.findAllByGroupId(groupId);
        return list;
    }

}