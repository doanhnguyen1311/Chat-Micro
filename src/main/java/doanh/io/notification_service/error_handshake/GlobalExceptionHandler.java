package doanh.io.notification_service.error_handshake;

import doanh.io.notification_service.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handleAllExceptions(Exception ex) {
        APIResponse<?> response = APIResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<APIResponse<?>> handleAccessDenied(Exception ex) {
        APIResponse<?> response = APIResponse.builder()
                .success(false)
                .message("Access Denied")
                .data(null)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<APIResponse<?>> handleUnauthorized(Exception ex) {
        APIResponse<?> response = APIResponse.builder()
                .success(false)
                .message("Unauthorized")
                .data(null)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
