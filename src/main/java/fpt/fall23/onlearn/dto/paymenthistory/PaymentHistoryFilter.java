package fpt.fall23.onlearn.dto.paymenthistory;

import fpt.fall23.onlearn.enums.PaymentHistoryStatus;
import fpt.fall23.onlearn.enums.PaymentHistoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class PaymentHistoryFilter{
    private String value;
    @Schema(description = "CREATED, COMPLETED, REFUNDED, PENDING_PAYOUT, COMPLETE_PAYOUT, PAYOUT, CANCEL")
    private String status;
    @Schema(description = "DEPOSIT, WITHDRAW")
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String orderBy;
}
