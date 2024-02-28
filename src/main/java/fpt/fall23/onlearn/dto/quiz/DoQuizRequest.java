package fpt.fall23.onlearn.dto.quiz;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class DoQuizRequest {
	private Long quizId;
	private Long enrollId;
	private String startTime;
	private List<DoQuizDetailRequest> doQuizDetailRequests;
}
