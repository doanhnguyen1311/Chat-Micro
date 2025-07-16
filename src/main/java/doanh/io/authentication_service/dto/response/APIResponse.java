package doanh.io.authentication_service.dto.response;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T>{
    private T data;
    private String message;
    private int statusCode;
    private boolean success;
}
