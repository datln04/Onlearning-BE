package fpt.fall23.onlearn.dto.quiz;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class QuizRequest {
    private Long id;
    private String title;
    private Double passScore;
    private String status;
    private Double duration;
//    private Date dateCreate;
    private Integer dateRange;
    private Integer allowAttempt;
    private Double proportion;
    private Long lessonId;
}
