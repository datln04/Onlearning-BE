package fpt.fall23.onlearn.dto.feedback;

import java.time.LocalDate;
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
public class DoFeedbackRequest {
	private Long id;
	private String content;
	private Boolean status;
	private LocalDate createDate;
	private Long enrollId;
	private List<FeedbackDetailRequest> feedbackDetailRequests;
}
