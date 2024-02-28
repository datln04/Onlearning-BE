package fpt.fall23.onlearn.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import fpt.fall23.onlearn.enums.WithdrawalRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "withdrawal_request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonView({WithdrawRequestView.class})
public class WithdrawalRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({WithdrawRequestView.class, TransactionView.class})
	private Long id;

	private Double withdrawalAmount;

	private LocalDateTime requestDate;

	@Enumerated(EnumType.STRING)
	private WithdrawalRequestStatus withdrawalRequestStatus;

	private String requestComments;

	// The teacher who made the withdrawal request
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;


	@OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY)
	private Set<Transaction> transactions;
}
