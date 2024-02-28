package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryView;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ EnrollView.class, ResultQuizView.class, PaymentHistoryView.class, TransactionView.class, FeedbackView.class, ReportView.class})
    private Long id;
    @JsonView({ FeedbackView.class, ResultQuizView.class, EnrollView.class})
    private String studentNumber;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView({ FeedbackView.class, ResultQuizView.class, EnrollView.class, ReportView.class})
    private Account account;

}
