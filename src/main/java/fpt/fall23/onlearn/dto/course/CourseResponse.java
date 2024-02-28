package fpt.fall23.onlearn.dto.course;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.gson.annotations.JsonAdapter;
import fpt.fall23.onlearn.entity.Course;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseResponse {

    @JsonUnwrapped()
    Course course;
    LocalDateTime finishDate;

}
