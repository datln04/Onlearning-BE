package fpt.fall23.onlearn.dto.question;

import fpt.fall23.onlearn.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRequest {
    private Long id;
    private String content;
    private Set<Answer> answers;
    private Integer courseId;
    private Integer lessonId;
}
