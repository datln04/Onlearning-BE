package fpt.fall23.onlearn.dto.paymenthistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentHistoryFilterStudent {
    private Long studentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
