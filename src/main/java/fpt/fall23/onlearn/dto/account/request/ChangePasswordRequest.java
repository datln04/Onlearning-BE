package fpt.fall23.onlearn.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
