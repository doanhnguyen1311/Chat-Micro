package doanh.io.graph_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse <T> {
    private int code;
    private String message;
    private T data;
    private boolean success;
}
