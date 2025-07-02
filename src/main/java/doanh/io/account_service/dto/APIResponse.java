package doanh.io.account_service.dto;

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
