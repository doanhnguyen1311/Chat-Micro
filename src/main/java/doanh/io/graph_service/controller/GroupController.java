package doanh.io.graph_service.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import doanh.io.graph_service.dto.APIResponse;
import doanh.io.graph_service.dto.CreateGroupRequest;
import doanh.io.graph_service.dto.SimpleJoinGroup;
import doanh.io.graph_service.entity.*;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.repository.GroupMembersRepository;
import doanh.io.graph_service.service.AuthenticationService;
import doanh.io.graph_service.service.GroupService;
import doanh.io.graph_service.messaging.GroupMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/groups")
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMessagingService groupMessagingService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    // Tạo mới một nhóm
    @PostMapping("")
    public APIResponse<Group> createGroup(
            @RequestBody CreateGroupRequest request,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is not set")
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
        group.setCreatedByUserId(authenticationService.decryptUserId(userId));
        Group createdGroup = groupService.createGroupWithMembers(group, memberIds);
        // Gửi thông báo tới các thành viên (trừ người tạo)
        if (memberIds != null) {
            for (String memberId : memberIds) {
                if (!memberId.equals(authenticationService.decryptUserId(userId))) {
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
                .success(true
                )
                .build();
    }

    // Cập nhật thông tin nhóm
    @PutMapping("/{id}")
    public APIResponse<Group> updateGroup(
            @PathVariable Long id,
            @RequestBody Group group,
            @CookieValue(value = "userId", required = false) String userId) {
        // Manual validation
        log.info(group.toString());
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
        log.info(group.toString());
        var groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty() || !groupOpt.get().getType().equals(GroupType.CHAT)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Group is not CHAT type")
                    .success(false)
                    .build();
        }
        if (!groupService.isAdmin(id, authenticationService.decryptUserId(userId))) {
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
        log.info(group.toString());
        var groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty() || !groupOpt.get().getType().equals(GroupType.SOCIAL)) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Group is not SOCIAL type")
                    .success(false)
                    .build();
        }
        if (!groupService.isAdmin(id, authenticationService.decryptUserId(userId))) {
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
        if (!groupService.isAdmin(id, authenticationService.decryptUserId(userId))) {
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
        if (group.getIsPrivate()) {
            if (group.getType() == GroupType.CHAT) {
                if (!groupService.isMember(id, authenticationService.decryptUserId(userId))) {
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
                .filter(g -> groupService.isMember(g.getId(), authenticationService.decryptUserId(userId)))
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
        List<Object> groups = groupService.getGroupsByUserId(authenticationService.decryptUserId(userId));
        return APIResponse.<List<Object>>builder()
                .code(HttpStatus.OK.value())
                .message("User's groups retrieved successfully")
                .data(groups)
                .success(true)
                .build();
    }

    // Lấy danh sách user trong một nhóm
    @GetMapping("/{id}/users")
    public APIResponse<?> getUsersInGroup(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        Optional<Group> groupOpt = groupService.getGroupById(id);
        if (groupOpt.isEmpty()) {
            return APIResponse.<List<GroupMembers>>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Group not found with id: " + id)
                    .success(false)
                    .build();
        }

        userId = authenticationService.decryptUserId(userId);
        Group group = groupOpt.get();
        if (group.getIsPrivate()) {
            if (!groupService.isMember(id, userId)) {
                return APIResponse.<List<GroupMembers>>builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("You are not a member of this private group")
                        .success(false)
                        .build();
            }
        } else {
            if (group.getType() == GroupType.CHAT && !groupService.isMember(id, userId)) {
                return APIResponse.<List<GroupMembers>>builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("You are not a member of this chat group")
                        .success(false)
                        .build();
            }
            // Nếu là SOCIAL và không private thì ai cũng xem được
        }
        List<GroupMembers> users = groupService.getGroupMembers(id);
        List<UserNode> listUsers = groupService.getUsersInGroup(id);

        return APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Users in group retrieved successfully")
                .data(Map.of(
                        "user-moderator", users,
                        "user-info", listUsers
                ))
                .success(true)
                .build();
    }

    // Đếm số lượng thành viên trong một nhóm
    @GetMapping("/{id}/members/count")
    public APIResponse<?> countMembersInGroup(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        userId = authenticationService.decryptUserId(userId);

        Optional<Group> groupOpt = groupService.getGroupById(id);

        if (groupOpt.isEmpty()) {
            return APIResponse.<Group>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Group not found with id: " + id)
                    .success(false)
                    .build();
        }

        Group group = groupOpt.get();

        if(group.getIsPrivate() && group.getType().equals(GroupType.CHAT)) {
            boolean isMember = groupService.isMember(id, userId);

            if(!isMember) {
                return APIResponse.builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("You are not a member of this group")
                        .success(false)
                        .build();
            }
        }

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
        boolean isMember = groupService.isMember(id, authenticationService.decryptUserId(userId));
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
        group.setCreatedByUserId(authenticationService.decryptUserId(userId));
        // Đảm bảo người tạo là ADMIN, các thành viên còn lại là MEMBER
        Group createdGroup = groupService.createGroupWithMembers(group, memberIds);
        return APIResponse.<Group>builder()
                .code(HttpStatus.CREATED.value())
                .message("Chat group created successfully")
                .data(createdGroup)
                .success(true)
                .build();
    }

    // Lấy danh sách bạn bè chưa có trong nhóm
    @GetMapping("/{id}/friends-not-in-group")
    public APIResponse<List<UserNode>> getFriendsNotInGroup(@PathVariable Long id, @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<List<UserNode>>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        List<UserNode> friends = groupService.getFriendsNotInGroup(authenticationService.decryptUserId(userId), id);
        return APIResponse.<List<UserNode>>builder()
                .code(HttpStatus.OK.value())
                .message("Friends not in group retrieved successfully")
                .data(friends)
                .success(true)
                .build();
    }

    // Thêm bạn bè vào nhóm
    @PostMapping("/{id}/add-friends")
    public APIResponse<Void> addFriendsToGroup(
            @PathVariable Long id,
            @RequestBody List<String> friendIds,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        // Chỉ cho phép thành viên nhóm hoặc admin thêm bạn
        if (!groupService.isMember(id, authenticationService.decryptUserId(userId))) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("You are not a member of this group")
                    .success(false)
                    .build();
        }
        try {
            groupService.addFriendsToGroup(id, friendIds, authenticationService.decryptUserId(userId));
            return APIResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Friends added to group successfully")
                    .success(true)
                    .build();
        } catch (RuntimeException e) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.ACCEPTED.value())
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @PostMapping("/{groupId}/change-moderator")
    public APIResponse<Void> changeModerator(
            @PathVariable Long groupId,
            @RequestParam String targetUserId,
            @RequestParam MemberRole targetRole,
            @CookieValue(value = "userId", required = false) String userId) {
        if (userId == null) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("User ID is required in cookie")
                    .success(false)
                    .build();
        }
        // Chỉ admin mới được thăng/hạ cấp
        if (!groupService.isAdmin(groupId, authenticationService.decryptUserId(userId))) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("You are not ADMIN of this group")
                    .success(false)
                    .build();
        }

        boolean isMember = groupService.isMember(groupId, targetUserId);

        if(!isMember){
            return APIResponse.<Void>builder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("This user is not a member of this group")
                    .success(false)
                    .build();
        }

        boolean ok = groupService.changeModeratorRole(groupId, targetUserId, targetRole);
        if (ok) {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Role updated successfully")
                    .success(true)
                    .build();
        } else {
            return APIResponse.<Void>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("User not found in group")
                    .success(false)
                    .build();
        }
    }


    /// rời nhóm hoặc bị kick khỏi nhóm

    @DeleteMapping("/leave/{id}")
    public APIResponse<?> leaveGroup(@CookieValue("userId") String userId, @PathVariable Long id){
        userId = authenticationService.decryptUserId(userId);

        boolean isAdmin = groupService.isAdmin(id, userId);

        if(isAdmin){
            return APIResponse.builder()
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .message("You need to transfer admin rights to someone else before leaving the group.")
                    .success(false)
                    .build();
        }

        boolean removeMember = groupService.removeUserFromGroup(id, userId);

        return APIResponse.builder()
                .data(removeMember)
                .code(HttpStatus.OK.value())
                .message("leave group successfully")
                .success(true)
                .build();
    }

    @DeleteMapping("/kick-user/{id}")
    public APIResponse<?> kickUser(@CookieValue(value = "userId", required = false) String userId, @PathVariable Long id, @RequestParam String targetUserId) {
        userId = authenticationService.decryptUserId(userId);

        var isMember = groupService.isMember(id, userId);

        if(userId.equals(targetUserId)){
            return APIResponse.builder()
                    .message("You can't kick self")
                    .success(false)
                    .code(400)
                    .data(null)
                    .build();
        }

        if(!isMember){
            return APIResponse.builder()
                    .message("You are not a member of this group")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .build();
        }

        var member = groupMembersRepository.findByUserIdAndGroupId(userId, id);

        log.info(member.getRole().toString());

        var infoUserDeleted = groupMembersRepository.findByUserIdAndGroupId(targetUserId, id);


        if(!member.getRole().toString().equals("ADMIN") && !member.getRole().toString().equals("MODERATOR")){
            return APIResponse.builder()
                    .message("You can't kick group member")
                    .build();
        }
        else{
            if(member.getRole().toString().equals("MODERATOR")){
                if(infoUserDeleted.getRole().toString().equals("MEMBER")){
                    boolean ok = groupService.removeUserFromGroup(id, targetUserId);
                    return APIResponse.builder()
                            .data(ok)
                            .code(HttpStatus.OK.value())
                            .message("kick user successfully")
                            .success(true)
                            .build();
                }
            }
            if(member.getRole().toString().equals("ADMIN")){
                boolean removeUser = groupService.removeUserFromGroup(id, targetUserId);

                return APIResponse.builder()
                        .data(removeUser)
                        .code(HttpStatus.OK.value())
                        .message("kick user successfully")
                        .success(true)
                        .build();
            }
            else{
                if(infoUserDeleted.getRole().toString().equals("MODERATOR") || infoUserDeleted.getRole().toString().equals("ADMIN")){
                    return APIResponse.builder()
                            .message("You can't kick group member")
                            .code(HttpStatus.FORBIDDEN.value())
                            .success(false)
                            .build();
                }
            }
        }
        return null;
    }

    @PostMapping("/join")
    public APIResponse<?> joinGroup(@CookieValue(value = "userId") String userId, @RequestBody SimpleJoinGroup payload) {
        userId = authenticationService.decryptUserId(userId);

        if(payload.getInviterId() == null || payload.getInviterId().isEmpty()){
            payload.setInviterId("");
        }

        Optional<Group> groupOpt = groupService.getGroupById(payload.getGroupId());

        boolean isMember = groupService.isMember(payload.getGroupId(), userId);

        if(isMember){
            return APIResponse.builder()
                    .data(null)
                    .message("You are already member of this group")
                    .success(false)
                    .code(HttpStatus.NOT_ACCEPTABLE.value())
                    .build();
        }

        var group = groupOpt.get();

        if(group.getType().toString().equals("SOCIAL")) {
            if (group.getJoinPolicy().toString().equals("OPEN")) {
                groupService.joinOpenGroup(payload.getGroupId(), userId);
                return APIResponse.builder()
                        .message("join group successfully")
                        .success(true)
                        .data(Map.of(
                                "type", "OPEN",
                                "status", "JOIN"
                        ))
                        .build();
            } else {
                if (group.getJoinPolicy().toString().equals("REQUEST")) {
                    var res = groupService.joinRequestGroup(payload.getGroupId(), userId, payload.getInviteType(), payload.getReason(), payload.getInviterId());

                    if (res) {
                        return APIResponse.builder()
                                .message("join request successfully")
                                .success(true)
                                .data(Map.of(
                                        "type", "REQUEST",
                                        "status", "WAITING"
                                ))
                                .build();
                    } else {
                        return APIResponse.builder()
                                .data(null)
                                .code(HttpStatus.FORBIDDEN.value())
                                .message("join request failed")
                                .success(false)
                                .build();
                    }
                }
            }
        }
        else{
            return APIResponse.builder()
                    .message("You can't join group!")
                    .success(false)
                    .code(400)
                    .data(null)
                    .build();
        }
        return null;
    }

    @GetMapping("/generate-link/{id}")
    public APIResponse<?> generateLinkInvite(@CookieValue(value = "userId") String userId, @PathVariable Long id) {

        var userIdDecrypt = authenticationService.decryptUserId(userId);

        boolean isMember = groupService.isMember(id, userIdDecrypt);

        if(!isMember){
            return APIResponse.builder()
                    .message("You are not a member of this group")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        SimpleJoinGroup simpleJoinGroup = SimpleJoinGroup.builder()
                .inviteType(InviteType.LINK)
                .reason("")
                .inviterId(userIdDecrypt)
                .groupId(id)
                .build();

        return APIResponse.builder()
                .data("https://com.delichat.online/graph/groups/invite-link?token=" + authenticationService.generateLinkToken(simpleJoinGroup))
                .success(true)
                .code(HttpStatus.OK.value())
                .message("invite link successfully")
                .build();
    }

    @GetMapping("/invite-link")
    public APIResponse<?> inviteLink(
            @CookieValue(value = "userId") String userId,
            @RequestParam String token
    ) throws ParseException, JOSEException {
        boolean verify = authenticationService.verifyToken(token);
        if (!verify) {
            return APIResponse.builder()
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .message("Invalid token")
                    .build();
        }

        SignedJWT signedJWT = SignedJWT.parse(token);

        String inviterId = signedJWT.getJWTClaimsSet().getClaim("inviterId").toString();
        Long groupId = Long.parseLong(signedJWT.getJWTClaimsSet().getClaim("groupId").toString());

        userId = authenticationService.decryptUserId(userId);

        if(inviterId.equals(userId)){
            return APIResponse.builder()
                    .message("You can't add you to this group")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        SimpleJoinGroup simpleJoinGroup = SimpleJoinGroup.builder()
                .inviteType(InviteType.LINK)
                .reason("")
                .inviterId(inviterId)
                .groupId(groupId)
                .build();

        boolean joined = groupService.processInviteLink(simpleJoinGroup, userId);

        return APIResponse.builder()
                .success(joined)
                .code(HttpStatus.OK.value())
                .message(joined ? "Joined group successfully" : "Request to join group pending approval")
                .build();
    }

    @GetMapping("/response-join/{id}")
    public APIResponse<?> responseJoinGroup(
            @CookieValue(value = "userId", required = false) String userId,
            @PathVariable Long id,
            @RequestParam String response,
            @RequestParam String targetUserId) {

        userId = authenticationService.decryptUserId(userId);
        var member = groupMembersRepository.findByUserIdAndGroupId(userId, id);

        if (member == null ||
                (!member.getRole().equals(MemberRole.MODERATOR) && !member.getRole().equals(MemberRole.ADMIN))) {
            return APIResponse.builder()
                    .message("You are not authorized!")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        boolean result;
        String message;

        switch (response.toLowerCase()) {
            case "accept" -> {
                result = groupService.acceptRequestJoinGroup(id, targetUserId);
                message = result ? "Accepted user successfully" : "Accept request failed";
            }
            case "reject" -> {
                result = groupService.rejectRequestJoinGroup(id, targetUserId);
                message = result ? "Rejected user successfully" : "Reject request failed";
            }
            default -> {
                return APIResponse.builder()
                        .success(false)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid response action")
                        .data(null)
                        .build();
            }
        }

        return APIResponse.builder()
                .success(result)
                .code(result ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(null)
                .build();
    }

    @GetMapping("/response-all-join/{id}")
    public APIResponse<?> responseAllJoinGroup(
            @CookieValue(value = "userId", required = false) String userId,
            @PathVariable Long id,
            @RequestParam String response) {

        userId = authenticationService.decryptUserId(userId);
        var member = groupMembersRepository.findByUserIdAndGroupId(userId, id);

        if (member == null ||
                (!member.getRole().equals(MemberRole.MODERATOR) && !member.getRole().equals(MemberRole.ADMIN))) {
            return APIResponse.builder()
                    .message("You are not authorized!")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        boolean result;
        String message;

        switch (response.toLowerCase()) {
            case "accept" -> {
                result = groupService.acceptAllRequestJoinGroup(id);
                message = result ? "Accepted all user successfully" : "Accept all request failed";
            }
            case "reject" -> {
                result = groupService.rejectAllRequestJoinGroup(id);
                message = result ? "Rejected all user successfully" : "Reject all request failed";
            }
            default -> {
                return APIResponse.builder()
                        .success(false)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid response action")
                        .data(null)
                        .build();
            }
        }

        return APIResponse.builder()
                .success(result)
                .code(result ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(null)
                .build();
    }

    @GetMapping("/all-pending/{id}")
    public APIResponse<?> allPendingGroup(@CookieValue(value = "userId", required = false) String userId, @PathVariable Long id) {
        userId = authenticationService.decryptUserId(userId);

        var member = groupMembersRepository.findByUserIdAndGroupId(userId, id);

        if (member == null ||
                (!member.getRole().equals(MemberRole.MODERATOR) && !member.getRole().equals(MemberRole.ADMIN))) {
            return APIResponse.builder()
                    .message("You are not authorized!")
                    .success(false)
                    .code(HttpStatus.FORBIDDEN.value())
                    .data(null)
                    .build();
        }

        var list = groupService.getAllPendingMembersByGroupId(id);

        return APIResponse.builder()
                .data(list)
                .success(true)
                .code(HttpStatus.OK.value())
                .message("All pending group successfully")
                .build();
    }

}