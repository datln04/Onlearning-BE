package fpt.fall23.onlearn.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.entity.UsedQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonView(ViewUsedQuestionDoQuiz.class)
public class ResponseList {
    private int code;
    private String message;
    private List<UsedQuestion> responseObjects;
}
