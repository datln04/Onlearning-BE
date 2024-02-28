package fpt.fall23.onlearn.dto.resultdetail;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.entity.ResultQuiz;
import fpt.fall23.onlearn.entity.UsedAnswer;
import fpt.fall23.onlearn.entity.UsedQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonView(ResultDetailView.class)
public class ResultDetailResponse {
    private Long id;
    private ResultQuiz resultQuiz;
    private UsedQuestion usedQuestion;
    private List<UsedAnswerResponse> usedAnswerResponses;
    private Boolean isCorrect;
}
