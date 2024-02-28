package fpt.fall23.onlearn.dto.quiz;

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
public class DoQuizDetailRequest {
	private Long questionId;
	private List<Long> answerIds;
}
