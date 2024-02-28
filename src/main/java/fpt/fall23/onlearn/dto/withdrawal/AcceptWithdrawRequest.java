package fpt.fall23.onlearn.dto.withdrawal;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class AcceptWithdrawRequest {
    private Long teacherId;
    private Long withdrawId;
}

