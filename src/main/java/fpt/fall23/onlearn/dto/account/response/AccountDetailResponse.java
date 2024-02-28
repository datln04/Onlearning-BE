package fpt.fall23.onlearn.dto.account.response;
import fpt.fall23.onlearn.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AccountDetailResponse {
    private Long id;

    private String avatar;

//    private Long studentId;

    private String username;

    private String phone;

    private String name;

    private String email;

    private String description;

    private Role role;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
    
    private Integer studentId;
    private Integer teacherId;
    private Integer staffId;

}
