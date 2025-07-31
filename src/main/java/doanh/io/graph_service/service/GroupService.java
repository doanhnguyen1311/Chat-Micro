package doanh.io.graph_service.service;

import doanh.io.graph_service.entity.Group;
import doanh.io.graph_service.entity.GroupMembers;
import doanh.io.graph_service.entity.GroupType;
import doanh.io.graph_service.entity.MemberRole;
import doanh.io.graph_service.node.GroupNode;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.repository.GroupMembersRepository;
import doanh.io.graph_service.repository.GroupNodeRepository;
import doanh.io.graph_service.repository.GroupRepository;
import doanh.io.graph_service.repository.UserNodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Tạo mới một nhóm
    @Transactional
    public Group createGroup(Group group) {
        // Đặt thời gian tạo và trạng thái mặc định
        group.setCreatedAt(LocalDateTime.now());
        group.setDeleted(false);

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

        groupNodeRepository.createOrUpdateGroupNode(savedGroup.getId(), savedGroup.getName(), savedGroup.getType().toString(), savedGroup.getAvatarUrl());

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
        group.setPrivate(updatedGroup.isPrivate());
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
        groupNodeRepository.createOrUpdateGroupNode(id, name, type, avatarUrl);
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
        group.setDeleted(true);

        // Lưu vào MySQL
        groupRepository.save(group);

        // Xóa node và các mối quan hệ trong Neo4j
        groupNodeRepository.deleteGroupAndRelationships(id);
    }

    // Lấy nhóm theo ID
    public Optional<Group> getGroupById(Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isPresent() && Boolean.TRUE.equals(groupOpt.get().isDeleted())) {
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
     * @param group thông tin nhóm
     * @param memberIds danh sách userId thành viên (ngoài người tạo)
     * @return nhóm đã tạo
     */
    @Transactional
    public Group createGroupWithMembers(Group group, List<String> memberIds) {
        group.setCreatedAt(LocalDateTime.now());
        group.setDeleted(false);
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
        groupNodeRepository.createOrUpdateGroupNode(savedGroup.getId(), savedGroup.getName(), savedGroup.getType().toString(), savedGroup.getAvatarUrl());
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
}