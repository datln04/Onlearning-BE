package fpt.fall23.onlearn.dto.used.question;


import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsedQuestionRequest {
    private Long id;
    private String content;
    private Boolean status;
    private Long quizId;
    private List<Long> usedAnswers;
}
