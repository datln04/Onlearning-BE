package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryView;
import fpt.fall23.onlearn.dto.question.QuestionView;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ CourseView.class, PaymentHistoryView.class, TransactionView.class , WithdrawRequestView.class, QuestionView.class, ReportView.class})
    private Long id;

    private String teacherNumber;

    private Double rating;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView({CourseView.class, ReportView.class})
    private Account account;
}
