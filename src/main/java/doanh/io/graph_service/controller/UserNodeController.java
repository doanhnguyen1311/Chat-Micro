package doanh.io.graph_service.controller;

import doanh.io.graph_service.dto.APIResponse;
import doanh.io.graph_service.node.UserNode;
import doanh.io.graph_service.repository.UserNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserNodeController {

    private final UserNodeRepository userNodeRepository;

    @PostMapping("/")
    public ResponseEntity<APIResponse<String>> addUserNode(@RequestBody UserNode userNode) {
        int result = userNodeRepository.createUser(userNode.getId(), userNode.getName(), userNode.getAddress(), userNode.getAvatar());
        if (result > 0) {
            return ResponseEntity.ok(
                    APIResponse.<String>builder()
                            .code(200)
                            .message("User created successfully")
                            .data(null)
                            .success(true)
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    APIResponse.<String>builder()
                            .code(400)
                            .message("Failed to create user. Possibly already exists.")
                            .data(null)
                            .success(false)
                            .build()
            );
        }
    }

    @PutMapping("/")
    public ResponseEntity<APIResponse<String>> updateUserNode(@RequestBody UserNode userNode) {
        // Lấy node hiện tại
        UserNode current = userNodeRepository.findById(userNode.getId()).orElse(null);
        if (current == null) {
            return ResponseEntity.badRequest().body(
                    APIResponse.<String>builder()
                            .code(404)
                            .message("User not found or update failed")
                            .data(null)
                            .success(false)
                            .build()
            );
        }
        // Nếu trường mới null thì giữ nguyên trường cũ
        String name = userNode.getName() != null ? userNode.getName() : current.getName();
        String address = userNode.getAddress() != null ? userNode.getAddress() : current.getAddress();
        String avatar = userNode.getAvatar() != null ? userNode.getAvatar() : current.getAvatar();
        int result = userNodeRepository.updateUser(userNode.getId(), name, address, avatar);
        if (result > 0) {
            return ResponseEntity.ok(
                    APIResponse.<String>builder()
                            .code(200)
                            .message("User updated successfully")
                            .data(null)
                            .success(true)
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    APIResponse.<String>builder()
                            .code(404)
                            .message("User not found or update failed")
                            .data(null)
                            .success(false)
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteUserNode(@PathVariable String id) {
        int result = userNodeRepository.deleteUserById(id);
        if (result > 0) {
            return ResponseEntity.ok(
                    APIResponse.<String>builder()
                            .code(200)
                            .message("User deleted successfully")
                            .data(null)
                            .success(true)
                            .build()
            );
        } else {
            return ResponseEntity.badRequest().body(
                    APIResponse.<String>builder()
                            .code(404)
                            .message("User not found or delete failed")
                            .data(null)
                            .success(false)
                            .build()
            );
        }
    }
}
