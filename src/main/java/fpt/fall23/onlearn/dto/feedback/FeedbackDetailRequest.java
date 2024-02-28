package fpt.fall23.onlearn.dto.feedback;

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
public class FeedbackDetailRequest {
	private Long id;
	private Long feedContentId;
	private int rate;
	private String type;
	private String text;
}
