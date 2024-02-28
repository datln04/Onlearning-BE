package fpt.fall23.onlearn.dto.paymenthistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PaymentHistoryRequest {
	
	private Long id;

	private String paymentMethod;

	private String transactionDate;

	private Double amount;

	@Schema(description = "SUCCESS or FAIL")
	private String paymentHistoryStatus;

	@Schema(description = "DEPOSIT or WITHDRAW")
	private String paymentHistoryType;

	private String cardDetails;

	private String bankInformation;

	private Long walletId; // recipient

	private Long studentId;

	private Long teacherId;

	private Long accountId;
}
