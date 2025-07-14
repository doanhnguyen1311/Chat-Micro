package doanh.io.account_service.controller;

import doanh.io.account_service.dto.APIResponse;
import doanh.io.account_service.dto.SecurityInfoDTO;
import doanh.io.account_service.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping
    public ResponseEntity<APIResponse<?>> getSecurityInfo(@RequestParam String email) {
        APIResponse<?> response = securityService.getSecurityInfo(email);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @GetMapping("/token-version")
    public ResponseEntity<APIResponse<?>> getTokenVersion(@RequestParam String email) {
        APIResponse<?> response = securityService.getTokenVersion(email);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PostMapping("/increment-login-attempts")
    public ResponseEntity<APIResponse<?>> incrementLoginAttempts(@RequestParam String email) {
        APIResponse<?> response = securityService.incrementLoginAttempts(email);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<APIResponse<?>> updateSecurity(
            @RequestParam String accountId,
            @RequestBody SecurityInfoDTO dto) {
        APIResponse<?> response = securityService.updateSecurity(accountId, dto);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }
}
