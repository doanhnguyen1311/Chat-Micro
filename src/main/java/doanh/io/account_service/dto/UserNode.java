package doanh.io.account_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNode {
    private String id;
    private String name;
    private String address;
    private String avatar;
}