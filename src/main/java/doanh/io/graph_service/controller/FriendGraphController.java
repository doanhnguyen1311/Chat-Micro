package doanh.io.graph_service.controller;

import doanh.io.graph_service.dto.APIResponse;
import doanh.io.graph_service.dto.RelationshipStatusResponse;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.repository.UserNodeRepository;
import doanh.io.graph_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationship")
@RequiredArgsConstructor
public class FriendGraphController {

    private final UserNodeRepository userNodeRepository;
    private final AuthenticationService authenticationService;

    private ResponseEntity<APIResponse<?>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                APIResponse.builder()
                        .code(401)
                        .success(false)
                        .message("Unauthorized: Missing userId cookie")
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/request")
    public ResponseEntity<APIResponse<?>> requestFriend(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String toUserId) {

        if (userId == null) return unauthorized();

        boolean exists = userNodeRepository.areFriends(authenticationService.decryptUserId(userId), toUserId) ||
                userNodeRepository.hasSentRequest(authenticationService.decryptUserId(userId), toUserId) ||
                userNodeRepository.hasReceivedRequest(authenticationService.decryptUserId(userId), toUserId);

        if (exists) {
            return ResponseEntity.badRequest().body(APIResponse.<String>builder()
                    .code(400)
                    .message("Relationship or request already exists")
                    .success(false)
                    .data(null)
                    .build());
        }

        userNodeRepository.sendFriendRequest(authenticationService.decryptUserId(userId), toUserId);
        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("Friend request sent.")
                .success(true)
                .data("REQUESTED")
                .build());
    }

    @DeleteMapping("/cancel-request")
    public ResponseEntity<APIResponse<String>> cancelRequest(
            @CookieValue(name = "userId", required = false) String selfId,
            @RequestParam String targetId
    ) {
        if (selfId == null || selfId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.<String>builder()
                    .code(401)
                    .message("Unauthorized")
                    .success(false)
                    .data(null)
                    .build());
        }

        int count = userNodeRepository.cancelFriendRequest(selfId, targetId);

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.<String>builder()
                    .code(404)
                    .message("No friend request found to cancel")
                    .success(false)
                    .data(null)
                    .build());
        }

        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("Friend request canceled")
                .success(true)
                .data("CANCELED")
                .build());
    }


    @PostMapping("/accept")
    public ResponseEntity<APIResponse<?>> acceptFriend(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String toUserId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        boolean received = userNodeRepository.hasReceivedRequest(userId, toUserId);
        if (!received) {
            return ResponseEntity.badRequest().body(APIResponse.<String>builder()
                    .code(400)
                    .message("No friend request to accept.")
                    .success(false)
                    .data(null)
                    .build());
        }

        userNodeRepository.acceptFriendRequest(toUserId, userId);
        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("Friend request accepted.")
                .success(true)
                .data("FRIENDS")
                .build());
    }

    @DeleteMapping("/reject")
    public ResponseEntity<APIResponse<?>> rejectFriend(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String toUserId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        boolean received = userNodeRepository.hasReceivedRequest(userId, toUserId);
        if (!received) {
            return ResponseEntity.badRequest().body(APIResponse.<String>builder()
                    .code(400)
                    .message("No friend request to reject.")
                    .success(false)
                    .data(null)
                    .build());
        }

        userNodeRepository.rejectFriendRequest(toUserId, userId);
        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("Friend request rejected.")
                .success(true)
                .data("REJECTED")
                .build());
    }

    @GetMapping("/status")
    public ResponseEntity<APIResponse<?>> getRelationshipStatus(
            @CookieValue(value = "userId", required = false) String uid,
            @RequestParam String targetId) {

        if (uid == null) return unauthorized();

        var userId = authenticationService.decryptUserId(uid);

        if (userId.equals(targetId)) {
            return ResponseEntity.ok(APIResponse.<RelationshipStatusResponse>builder()
                    .code(200)
                    .message("Viewing own profile.")
                    .success(true)
                    .data(new RelationshipStatusResponse("SELF"))
                    .build());
        }

        boolean areFriends = Boolean.TRUE.equals(userNodeRepository.areFriends(userId, targetId));
        boolean hasSent = Boolean.TRUE.equals(userNodeRepository.hasSentRequest(userId, targetId));
        boolean hasReceived = Boolean.TRUE.equals(userNodeRepository.hasReceivedRequest(userId, targetId));
        boolean youBlockedThem = Boolean.TRUE.equals(userNodeRepository.isBlocked(userId, targetId));
        boolean theyBlockedYou = Boolean.TRUE.equals(userNodeRepository.isBlocked(targetId, userId));

        String status;
        if (theyBlockedYou) status = "BLOCKED_BY_THEM";
        else if (youBlockedThem) status = "YOU_BLOCKED";
        else if (areFriends) status = "FRIENDS";
        else if (hasSent) status = "REQUEST_SENT";
        else if (hasReceived) status = "REQUEST_RECEIVED";
        else status = "NONE";

        return ResponseEntity.ok(APIResponse.<RelationshipStatusResponse>builder()
                .code(200)
                .message("Relationship status checked.")
                .success(true)
                .data(new RelationshipStatusResponse(status))
                .build());
    }

    @PostMapping("/block")
    public ResponseEntity<APIResponse<?>> blockUser(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String toUserId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);
        if (userId.equals(toUserId)) {
            return ResponseEntity.badRequest().body(APIResponse.<String>builder()
                    .code(400)
                    .message("Cannot block yourself.")
                    .success(false)
                    .data(null)
                    .build());
        }

        userNodeRepository.blockUser(userId, toUserId);
        userNodeRepository.removeFriend(userId, toUserId); // huỷ kết bạn nếu đang là bạn

        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("User blocked successfully.")
                .success(true)
                .data("BLOCKED")
                .build());
    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<APIResponse<String>> unfriend(
            @CookieValue(name = "userId", required = false) String selfId,
            @RequestParam String targetId
    ) {
        if (selfId == null || selfId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.<String>builder()
                    .code(401)
                    .message("Unauthorized")
                    .success(false)
                    .data(null)
                    .build());
        }
        selfId = authenticationService.decryptUserId(selfId);

        Long deletedCount = userNodeRepository.deleteFriendship(selfId, targetId);

        if (deletedCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.<String>builder()
                    .code(404)
                    .message("No friendship found to delete")
                    .success(false)
                    .data(null)
                    .build());
        }

        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("Unfriended successfully")
                .success(true)
                .data("UNFRIENDED")
                .build());
    }

    @DeleteMapping("/block")
    public ResponseEntity<APIResponse<?>> unblockUser(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String toUserId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        Boolean isBlocked = userNodeRepository.isBlocked(userId, toUserId);
        if (Boolean.FALSE.equals(isBlocked)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.<String>builder()
                    .code(403)
                    .message("You cannot unblock because you are not the blocker.")
                    .success(false)
                    .data(null)
                    .build());
        }

        userNodeRepository.unblockUser(userId, toUserId);
        return ResponseEntity.ok(APIResponse.<String>builder()
                .code(200)
                .message("User unblocked successfully.")
                .success(true)
                .data("UNBLOCKED")
                .build());
    }

    @GetMapping("/mutual-friends")
    public ResponseEntity<APIResponse<?>> getMutualFriends(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String targetId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        List<UserNode> mutualFriends = userNodeRepository.findMutualFriends(userId, targetId);
        return ResponseEntity.ok(APIResponse.<List<UserNode>>builder()
                .code(200)
                .message("Lấy danh sách bạn chung thành công.")
                .success(true)
                .data(mutualFriends)
                .build());
    }

    @GetMapping("/mutual-friends/count")
    public ResponseEntity<APIResponse<?>> getMutualFriendCount(
            @CookieValue(value = "userId", required = false) String userId,
            @RequestParam String targetId) {

        if (userId == null) return unauthorized();
        userId = authenticationService.decryptUserId(userId);


        Long count = userNodeRepository.countMutualFriends(userId, targetId);
        return ResponseEntity.ok(APIResponse.<Long>builder()
                .code(200)
                .message("Lấy số lượng bạn chung thành công.")
                .success(true)
                .data(count)
                .build());
    }

    @GetMapping("/friends")
    public ResponseEntity<APIResponse<?>> getFriends(
            @CookieValue(value = "userId", required = false) String userId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        List<UserNode> friends = userNodeRepository.findFriends(userId);
        return ResponseEntity.ok(new APIResponse<>(200, "Lấy danh sách bạn bè thành công", friends, true));
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<APIResponse<?>> getSentRequests(
            @CookieValue(value = "userId", required = false) String userId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        List<UserNode> sent = userNodeRepository.findFriendRequestsSent(userId);
        return ResponseEntity.ok(new APIResponse<>(200, "Lấy danh sách lời mời đã gửi thành công", sent, true));
    }

    @GetMapping("/requests/received")
    public ResponseEntity<APIResponse<?>> getReceivedRequests(
            @CookieValue(value = "userId", required = false) String userId) {

        if (userId == null) return unauthorized();

        userId = authenticationService.decryptUserId(userId);

        List<UserNode> received = userNodeRepository.findFriendRequestsReceived(userId);
        return ResponseEntity.ok(new APIResponse<>(200, "Lấy danh sách lời mời đã nhận thành công", received, true));
    }
}
