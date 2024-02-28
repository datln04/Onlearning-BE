package fpt.fall23.onlearn.dto.used.answer;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsedAnswerRequest {
    private Long id;
    private String content;
    private Boolean isCorrect;
//    private Long usedQuestionId;
}
