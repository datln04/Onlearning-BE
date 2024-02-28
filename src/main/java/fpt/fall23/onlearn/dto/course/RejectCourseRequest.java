package fpt.fall23.onlearn.dto.course;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class RejectCourseRequest {
    private Long courseId;
    private String reason;
}
