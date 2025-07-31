package doanh.io.graph_service.controller;

import doanh.io.graph_service.dto.APIResponse;
import doanh.io.graph_service.dto.CreateGroupRequest;
import doanh.io.graph_service.entity.Group;
import doanh.io.graph_service.entity.GroupType;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.service.GroupService;
import doanh.io.graph_service.messaging.GroupMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMessagingService groupMessagingService;

    // Tạo mới một nhóm
    @PostMapping("")
    public APIResponse<Group> createGroup(
            @RequestBody CreateGroupRequest request,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        Group group = request.getGroup();
        List<String> memberIds = request.getMemberIds();
        if (group == null || group.getName() == null || group.getName().trim().isEmpty()) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Name is required")
                    .success(false)
                    .build();
        }
        if (group.getType() == null) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Type is required")
                    .success(false)
                    .build();
        }
        // Nếu là nhóm CHAT thì phải có ít nhất 3 thành viên (bao gồm người tạo)
        if (group.getType().equals(GroupType.CHAT)) {
            int totalMembers = (memberIds == null ? 0 : memberIds.size()) + 1;
            if (totalMembers < 3) {
                return APIResponse.<Group>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Nhóm chat phải có ít nhất 3 thành viên (bao gồm người tạo nhóm)")
                        .success(false)
                        .build();
            }
        }
        group.setCreatedByUserId(userId);
        Group createdGroup = groupService.createGroupWithMembers(group, memberIds);
        // Gửi thông báo tới các thành viên (trừ người tạo)
        if (memberIds != null) {
            for (String memberId : memberIds) {
                if (!memberId.equals(userId)) {
                    groupMessagingService.sendCreateChatRoom(memberId,
                            APIResponse.builder()
                                    .code(HttpStatus.OK.value())
                                    .data(createdGroup)
                                    .success(true)
                                    .message("createChatRoom success")
                                    .build());
                    groupMessagingService.sendNotify(memberId,
                            APIResponse.builder()
                                    .data(null)
                                    .message("Bạn đã được thêm vào nhóm: "  + createdGroup.getName())
                                    .success(true)
                                    .build()
                            );
                }
            }
        }
        return APIResponse.<Group>builder()
                .code(HttpStatus.CREATED.value())
                .message("Group created successfully")
                .data(createdGroup)
                .success(true)
                .build();
    }

    // Cập nhật thông tin nhóm
    @PutMapping("/{id}")
    public APIResponse<Group> updateGroup(
            @PathVariable Long id,
            @RequestBody Group group,
            @CookieValue(value = "userId", required = false) String userId) {
        // Manual validation
        if (userId == null) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        // Optional: Validate fields if they are provided
        if (group.getName() != null && group.getName().trim().isEmpty()) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Name cannot be empty")
                    .success(false)
                    .build();
        }

        Group updatedGroup = groupService.updateGroup(id, group);
        return APIResponse.<Group>builder()
                .code(HttpStatus.OK.value())
                .message("Group updated successfully")
                .data(updatedGroup)
                .success(true)
                .build();
    }

    // Cập nhật nhóm CHAT
    @PutMapping("/{id}/update-chat")
    public APIResponse<Group> updateChatGroup(
            @PathVariable Long id,
            @RequestBody Group group,
            @CookieValue(value = "userId", required = false) String userId) {
        var groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty() || !groupOpt.get().getType().equals(GroupType.CHAT)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Group is not CHAT type")
                    .success(false)
                    .build();
        }
        if (!groupService.isAdmin(id, userId)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("You are not ADMIN of this group")
                    .success(false)
                    .build();
        }
        Group updatedGroup = groupService.updateGroup(id, group);
        return APIResponse.<Group>builder()
                .code(HttpStatus.OK.value())
                .message("Chat group updated successfully")
                .data(updatedGroup)
                .success(true)
                .build();
    }

    // Cập nhật nhóm SOCIAL
    @PutMapping("/{id}/update-social")
    public APIResponse<Group> updateSocialGroup(
            @PathVariable Long id,
            @RequestBody Group group,
            @CookieValue(value = "userId", required = false) String userId) {
        var groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty() || !groupOpt.get().getType().equals(GroupType.SOCIAL)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Group is not SOCIAL type")
                    .success(false)
                    .build();
        }
        if (!groupService.isAdmin(id, userId)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("You are not ADMIN of this group")
                    .success(false)
                    .build();
        }
        Group updatedGroup = groupService.updateGroup(id, group);
        return APIResponse.<Group>builder()
                .code(HttpStatus.OK.value())
                .message("Social group updated successfully")
                .data(updatedGroup)
                .success(true)
                .build();
    }

    // Xóa mềm một nhóm
    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteGroup(
            @PathVariable Long id,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        if (!groupService.isAdmin(id, userId)) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("You are not ADMIN of this group")
                    .success(false)
                    .build();
        }
        groupService.softDeleteGroup(id);
        return APIResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Group deleted successfully")
                .success(true)
                .build();
    }

    // Lấy thông tin nhóm theo ID
    @GetMapping("/{id}")
    public APIResponse<Group> getGroupById(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        Optional<Group> groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty()) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Group not found with id: " + id)
                    .success(false)
                    .build();
        }
        Group group = groupOpt.get();
        if (group.isPrivate()) {
            if (group.getType() == GroupType.CHAT) {
                if (!groupService.isMember(id, userId)) {
                    return APIResponse.<Group>builder()
                            .code(HttpStatus.FORBIDDEN.value())
                            .message("You are not a member of this private chat group")
                            .success(false)
                            .build();
                }
            } else if (group.getType() == GroupType.SOCIAL) {
                // Chỉ cần kiểm tra private, không cần kiểm tra thành viên
                return APIResponse.<Group>builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("This social group is private")
                        .success(false)
                        .build();
            }
        }
        return APIResponse.<Group>builder()
                .code(HttpStatus.OK.value())
                .message("Group retrieved successfully")
                .data(group)
                .success(true)
                .build();
    }

    // Lấy danh sách nhóm theo loại (CHAT hoặc SOCIAL)
    @GetMapping("/type/{type}")
    public APIResponse<List<Group>> getGroupsByTypeForUser(@PathVariable String type, @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<List<Group>>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        try {
            GroupType groupType = GroupType.valueOf(type.toUpperCase());
            List<Group> allGroups = groupService.getGroupsByType(groupType);
            // Lọc ra các nhóm mà user là thành viên
            List<Group> userGroups = allGroups.stream()
                .filter(g -> groupService.isMember(g.getId(), userId))
                .toList();
            return APIResponse.<List<Group>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Groups retrieved successfully")
                    .data(userGroups)
                    .success(true)
                    .build();
        } catch (IllegalArgumentException e) {
            return APIResponse.<List<Group>>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Invalid group type: " + type)
                    .success(false)
                    .build();
        }
    }

    // Lấy danh sách nhóm mà một user tham gia
    @GetMapping("/user/{userId}")
    public APIResponse<List<Object>> getGroupsByUserId(@PathVariable String userId) {
        List<Object> groups = groupService.getGroupsByUserId(userId);
        return APIResponse.<List<Object>>builder()
                .code(HttpStatus.OK.value())
                .message("User's groups retrieved successfully")
                .data(groups)
                .success(true)
                .build();
    }

    // Lấy danh sách user trong một nhóm
    @GetMapping("/{id}/users")
    public APIResponse<List<UserNode>> getUsersInGroup(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        Optional<Group> groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty()) {
            return APIResponse.<List<UserNode>>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Group not found with id: " + id)
                    .success(false)
                    .build();
        }
        Group group = groupOpt.get();
        if (group.isPrivate()) {
            if (!groupService.isMember(id, userId)) {
                return APIResponse.<List<UserNode>>builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("You are not a member of this private group")
                        .success(false)
                        .build();
            }
        } else {
            if (group.getType() == GroupType.CHAT && !groupService.isMember(id, userId)) {
                return APIResponse.<List<UserNode>>builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("You are not a member of this chat group")
                        .success(false)
                        .build();
            }
            // Nếu là SOCIAL và không private thì ai cũng xem được
        }
        List<UserNode> users = groupService.getUsersInGroup(id);
        return APIResponse.<List<UserNode>>builder()
                .code(HttpStatus.OK.value())
                .message("Users in group retrieved successfully")
                .data(users)
                .success(true)
                .build();
    }

    // Đếm số lượng thành viên trong một nhóm
    @GetMapping("/{id}/members/count")
    public APIResponse<Long> countMembersInGroup(@PathVariable Long id) {
        Long count = groupService.countMembersInGroup(id);
        return APIResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .message("Member count retrieved successfully")
                .data(count)
                .success(true)
                .build();
    }

    // Kiểm tra người dùng hiện tại có phải thành viên nhóm không
    @GetMapping("/{id}/is-member")
    public APIResponse<Boolean> isUserMemberOfGroup(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        boolean isMember = groupService.isMember(id, userId);
        return APIResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .message(isMember ? "User is a member of the group" : "User is not a member of the group")
                .data(isMember)
                .success(true)
                .build();
    }

    // Tạo nhóm chat
    @PostMapping("/create-chat-group")
    public APIResponse<Group> createChatGroup(
            @RequestBody CreateGroupRequest request,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        Group group = request.getGroup();
        List<String> memberIds = request.getMemberIds();
        if (group == null || group.getName() == null || group.getName().trim().isEmpty()) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Name is required")
                    .success(false)
                    .build();
        }
        if (group.getType() == null || !group.getType().equals(GroupType.CHAT)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Group type must be CHAT")
                    .success(false)
                    .build();
        }
        // Đảm bảo có ít nhất 3 thành viên (bao gồm người tạo)
        int totalMembers = (memberIds == null ? 0 : memberIds.size()) + 1;
        if (totalMembers < 3) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Nhóm chat phải có ít nhất 3 thành viên (bao gồm người tạo nhóm)")
                    .success(false)
                    .build();
        }
        group.setCreatedByUserId(userId);
        // Đảm bảo người tạo là ADMIN, các thành viên còn lại là MEMBER
        Group createdGroup = groupService.createGroupWithMembers(group, memberIds);
        return APIResponse.<Group>builder()
                .code(HttpStatus.CREATED.value())
                .message("Chat group created successfully")
                .data(createdGroup)
                .success(true)
                .build();
    }
}