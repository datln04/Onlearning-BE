package fpt.fall23.onlearn.dto.quiz.response;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.quiz.ViewQuiz;
import fpt.fall23.onlearn.dto.used.question.ViewUsedQuestion;
import fpt.fall23.onlearn.entity.Lesson;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class QuizResponse {

    @JsonView({ViewUsedQuestion.class, ViewQuiz.class, ResultQuizView.class, ViewUsedQuestionDoQuiz.class})
    private Long id;

    @JsonView({ResultQuizView.class, ViewQuiz.class})
    private String title;

    private Double passScore;

    private String status;

    private Double duration;

    private LocalDateTime dateCreate;

    private Integer dateRange;

    private Integer allowAttempt;

    private Integer attemptTime;

    private Double proportion;

    private Lesson lesson;

}
