package doanh.io.notification_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T> {
    private String message;
    private boolean success;
    private T data;
    private int statusCode;
}
