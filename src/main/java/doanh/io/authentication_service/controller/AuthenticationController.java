package doanh.io.authentication_service.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import doanh.io.authentication_service.dto.request.LoginRequest;
import doanh.io.authentication_service.dto.response.APIResponse;
import doanh.io.authentication_service.dto.response.AuthenticatedResponse;
import doanh.io.authentication_service.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private String userIdFromCookie(String refreshToken) throws ParseException {
        var decodeRefreshToken = SignedJWT.parse(refreshToken);

        var userIdLeader = decodeRefreshToken.getJWTClaimsSet().getClaim("userId");

        return (String)userIdLeader;
    }


    @PostMapping("/login")
    public ResponseEntity<APIResponse<?>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse responseCookie,
            @CookieValue(value = "deviceId", required = false) String deviceId
    ) throws ParseException {
        // Nếu chưa có deviceId thì tạo mới
        if (deviceId == null || deviceId.isBlank()) {
            deviceId = UUID.randomUUID().toString();

            Cookie deviceIdCookie = new Cookie("deviceId", deviceId);
            deviceIdCookie.setHttpOnly(true);
            deviceIdCookie.setSecure(true);
            deviceIdCookie.setPath("/");
            deviceIdCookie.setMaxAge(3600 * 24 * 60); // 60 ngày

            responseCookie.addCookie(deviceIdCookie);
        }

        loginRequest.setDeviceId(deviceId);

        APIResponse<AuthenticatedResponse> response = authenticationService.Login(loginRequest);

        if (response.getData() != null) {
            Cookie token = new Cookie("accessToken", response.getData().getToken());
            token.setHttpOnly(true);
            token.setSecure(true);
            token.setPath("/");
            token.setMaxAge(3600 * 24 * 2); // 2 ngày

            Cookie userId = new Cookie("userId", userIdFromCookie(response.getData().getToken()));
            userId.setHttpOnly(true);
            userId.setSecure(true);
            userId.setPath("/");
            userId.setMaxAge(3600 * 24 * 2);

            responseCookie.addCookie(userId);
            responseCookie.addCookie(token);
        }

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            int statusCode = response.getStatusCode() != 0 ? response.getStatusCode() : HttpStatus.BAD_REQUEST.value();
            return ResponseEntity.status(statusCode).body(response);
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<APIResponse<?>> refreshToken(
            @CookieValue("accessToken") String accessToken,
            @CookieValue("deviceId") String deviceId) {
        try {
            String token = authenticationService.refreshToken(accessToken, deviceId);
            if (token != null) {
                return ResponseEntity.ok(APIResponse.builder()
                                .success(true)
                                .data(token)
                                .message("Refresh token expired")
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (ParseException | JOSEException e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(APIResponse.builder()
                            .success(false)
                            .message("Failed to refresh token: " + e.getMessage())
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .build());
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<APIResponse<?>> verifyToken(@CookieValue("accessToken") String token) {
        try {
            APIResponse<?> response = authenticationService.verifyToken(token);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                int statusCode = response.getStatusCode() != 0 ? response.getStatusCode() : HttpStatus.UNAUTHORIZED.value();
                return ResponseEntity.status(statusCode).body(response);
            }
        } catch (ParseException | JOSEException e) {
            log.error("Error verifying token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(APIResponse.builder()
                            .success(false)
                            .message("Invalid token format or signature: " + e.getMessage())
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .build());
        }
    }

    @GetMapping("/is-token-expiry")
    public ResponseEntity<APIResponse<Boolean>> isTokenExpiry(@CookieValue("accessToken") String token) {
        boolean isExpired = authenticationService.isTokenExpiry(token);
        return ResponseEntity.ok(APIResponse.<Boolean>builder()
                .data(isExpired)
                .message(isExpired ? "Token is expired." : "Token is valid.")
                .success(true)
                .statusCode(200)
                .build());
    }

    @GetMapping("/my-info")
    public ResponseEntity<APIResponse<?>> getAccountById(@CookieValue("accessToken") String accessToken) throws ParseException {
        String id = userIdFromCookie(accessToken);

        APIResponse<?> response = authenticationService.getAccount(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<?>> logout(
            @CookieValue("accessToken") String accessToken,
            @CookieValue("deviceId") String deviceId,
            HttpServletResponse response
    ) {
        try {
            String userId = userIdFromCookie(accessToken);
            authenticationService.logout(userId, deviceId);

            // Xóa cookie bằng cách set Max-Age = 0
            clearCookie("accessToken", response);
            clearCookie("deviceId", response);

            return ResponseEntity.ok(APIResponse.builder()
                    .success(true)
                    .message("Logged out successfully.")
                    .statusCode(200)
                    .build());

        } catch (Exception e) {
            log.error("Error during logout for user {}: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.builder()
                            .success(false)
                            .message("Logout failed: " + e.getMessage())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    private void clearCookie(String name, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @PostMapping("/logout-all-device")
    public ResponseEntity<APIResponse<?>> logoutAllDevice(@CookieValue(value = "accessToken", required = false) String accessToken, @CookieValue(value = "deviceId", required = false) String deviceId) {
        try{
            String userId = userIdFromCookie(accessToken);

            var res = authenticationService.logoutAllDevices(userId, deviceId);

            simpMessagingTemplate.convertAndSend(
                    "/topic/logout-all/" + userId,
                    APIResponse.builder()
                            .success(true)
                            .message("Logged out successfully.")
                            .build()
            );

            return ResponseEntity.ok(res);
        }
        catch (Exception e) {
            log.error("Error during logout all device: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.builder()
                            .message("Logout failed: " + e.getMessage())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .success(false)
                            .build());
        }
    }

    @PostMapping("/auto-login")
    public ResponseEntity<APIResponse<?>> loginWithToken(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            @CookieValue(value = "deviceId", required = false) String deviceId,
            HttpServletResponse responseCookie
    ) {
        try {
            // Nếu chưa có deviceId -> tạo mới + gắn cookie
            if (deviceId == null || deviceId.isBlank()) {
                deviceId = UUID.randomUUID().toString();

                Cookie deviceIdCookie = new Cookie("deviceId", deviceId);
                deviceIdCookie.setHttpOnly(true);
                deviceIdCookie.setSecure(true);
                deviceIdCookie.setPath("/");
                deviceIdCookie.setMaxAge(3600 * 24 * 60); // 60 ngày

                responseCookie.addCookie(deviceIdCookie);
            }
            if(accessToken == null || accessToken.isEmpty() || accessToken == "") {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.builder()
                                .data(null)
                                .success(false)
                                .message("Invalid access token")
                        .build());
            }

            APIResponse<AuthenticatedResponse> res = authenticationService.introspect(accessToken, deviceId);

            if(!res.isSuccess()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            Cookie token = new Cookie("accessToken", res.getData().getToken());
            token.setHttpOnly(true);
            token.setSecure(true);
            token.setPath("/");
            token.setMaxAge(3600 * 24 * 2);
            responseCookie.addCookie(token); // <-- THIẾU DÒNG NÀY TRƯỚC ĐÂY

            res.setData(null); // Không trả token ra ngoài
            return ResponseEntity.status(res.getStatusCode()).body(res);
        } catch (Exception e) {
            log.error("Error during login with token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResponse.builder()
                            .success(false)
                            .message("Error system")
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @GetMapping("/get-device")
    public ResponseEntity<APIResponse<?>> getDevice(@CookieValue(value = "deviceId", required = false) String deviceId, HttpServletResponse responseCookie) {
        if(deviceId == null || deviceId.isBlank()) {
            deviceId = UUID.randomUUID().toString();
            Cookie deviceIdCookie = new Cookie("deviceId", deviceId);
            deviceIdCookie.setHttpOnly(true);
            deviceIdCookie.setSecure(true);
            deviceIdCookie.setPath("/");
            deviceIdCookie.setMaxAge(3600 * 24);
            responseCookie.addCookie(deviceIdCookie);
        }

        return ResponseEntity.ok(
                APIResponse.builder()
                        .data(deviceId)
                        .success(true)
                        .message("Get device successfully.")
                        .statusCode(200)
                        .build()
        );
    }


}