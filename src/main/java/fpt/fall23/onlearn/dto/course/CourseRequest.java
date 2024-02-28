package fpt.fall23.onlearn.dto.course;

import fpt.fall23.onlearn.enums.CourseStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseRequest {

    private Long id;

    private String name;

    private CourseStatus status;

    private String image;

    private String description;

    private LocalDate createDate;

    private Double price;

    private Integer limitTime;

    private Double averagePoint;

    private Long teacherId;

    private Long subjectId;

//    private Set<Lesson> lessons;

//    private List<String> lessonIds;

}
