package fpt.fall23.onlearn.entity;


import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.enums.EnrollStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "enroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({EnrollView.class})
public class Enroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EnrollView.class, FeedbackView.class, ResultQuizView.class, TransactionView.class})
    private Long id;
    private LocalDateTime requestDate;
    private Double amount;
    private String paymentStatus;

    private Double commission;

    private Double commissionAmount;

    @Enumerated(EnumType.STRING)
    private EnrollStatus status;

    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @JsonView({FeedbackView.class, EnrollView.class})
    private Student student;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @JsonView({FeedbackView.class, EnrollView.class})
    private Course course;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "syllabus_id")
    @JsonView({FeedbackView.class, EnrollView.class})
    private Syllabus syllabus;

    @Column(name = "is_banned")
    private boolean isBanned = false;

}
