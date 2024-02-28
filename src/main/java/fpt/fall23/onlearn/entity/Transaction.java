package fpt.fall23.onlearn.entity;

import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.dto.syllabus.SyllabusView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import fpt.fall23.onlearn.enums.LessonType;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.transaction.TransactionView;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({TransactionView.class})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({TransactionView.class, WithdrawRequestView.class})
    private Long id;

    private String accountName;

    private Double amount;

    private LocalDateTime dateProcess;


    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Long parentId;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(targetEntity = Teacher.class)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    private Enroll enroll;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdraw_request_id")
    private WithdrawalRequest withdrawalRequests;

}
