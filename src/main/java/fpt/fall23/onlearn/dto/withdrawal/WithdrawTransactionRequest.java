package fpt.fall23.onlearn.dto.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WithdrawTransactionRequest {
    private Long teacherId;
    private List<Long> transactions;
}
