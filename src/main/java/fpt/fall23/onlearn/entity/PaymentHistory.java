package fpt.fall23.onlearn.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import fpt.fall23.onlearn.enums.PaymentHistoryStatus;
import fpt.fall23.onlearn.enums.PaymentHistoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({PaymentHistoryView.class, WithdrawRequestView.class})
public class PaymentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String paymentMethod;

	private LocalDateTime transactionDate;

	private Double amount;

	@Enumerated(EnumType.STRING)
	private PaymentHistoryStatus paymentHistoryStatus;

	@Enumerated(EnumType.STRING)
	private PaymentHistoryType paymentHistoryType;

	private String cardDetails;

	private String bankInformation;

	private String orderId;

	private String captureId;

	private String payoutBatchId;

	private String payoutBatchItemId;

	@ManyToOne(targetEntity = Wallet.class)
	@JoinColumn(name = "wallet_id")
	private Wallet wallet; // recipient

	@ManyToOne(targetEntity = Student.class)
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne(targetEntity = Teacher.class)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(name = "account_id")
	private Account account;

}
