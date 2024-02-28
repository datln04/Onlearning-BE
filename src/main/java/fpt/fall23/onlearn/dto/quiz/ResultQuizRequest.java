package fpt.fall23.onlearn.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultQuizRequest {
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    private String resultStatus;

    private Double lastPoint;

    private Long studentId;

    private Long enrollId;

    private Long quizId;
}
