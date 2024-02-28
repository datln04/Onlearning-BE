package fpt.fall23.onlearn.dto.resultdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.used.question.ViewUsedQuestion;
import fpt.fall23.onlearn.entity.UsedQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class UsedAnswerResponse {
    @JsonView(ResultDetailView.class)
    private Long id;
    @JsonView(ResultDetailView.class)
    private String content;
    @JsonView(ResultDetailView.class)
    private Boolean isCorrect;
    @JsonView(ResultDetailView.class)
    private Boolean isChoose;

    private UsedQuestion usedQuestion;
}
