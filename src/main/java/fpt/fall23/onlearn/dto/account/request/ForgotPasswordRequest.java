package fpt.fall23.onlearn.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordRequest {
//    private Long accountId;
    private String email;
}
