package fpt.fall23.onlearn.dto.lesson;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class LessonRequest {

    private Long id;

    private String name;

    private String status;

    private String description;

    private String url;

    private LocalDate dateTime;

    private Integer estimateTime;

    private Integer courseId;

    private String content;

    @Schema(description = "Type is VIDEO or READING")
    private String type;

//	    private List<String> resourseIds;

    private List<String> syllabusIds;

}
