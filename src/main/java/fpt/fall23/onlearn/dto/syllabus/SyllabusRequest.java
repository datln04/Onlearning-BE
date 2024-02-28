package fpt.fall23.onlearn.dto.syllabus;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SyllabusRequest {

	private Long id;
	private String name;
	private Long courseId;
	private List<Long> lessonIds;
	private String status;
}
