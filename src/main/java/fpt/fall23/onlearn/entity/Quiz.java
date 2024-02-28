package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.quiz.ViewQuiz;
import fpt.fall23.onlearn.dto.used.question.ViewUsedQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(ViewQuiz.class)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ViewUsedQuestion.class, ViewQuiz.class, ResultQuizView.class, ViewUsedQuestionDoQuiz.class})
    private Long id;

    @JsonView({ResultQuizView.class, ViewQuiz.class})
    private String title;

    private Double passScore;

    private String status;

    private Double duration;

    private LocalDate dateCreate;

    private Integer dateRange;

    private Integer allowAttempt;


    private Double proportion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}
