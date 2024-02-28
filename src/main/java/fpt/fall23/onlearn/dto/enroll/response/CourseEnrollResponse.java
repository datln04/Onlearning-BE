package fpt.fall23.onlearn.dto.enroll.response;

import fpt.fall23.onlearn.entity.Course;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollResponse {
    private Long id;

    private String name;

    private String status;

    private String image;

    private String description;

    private LocalDateTime createDate;

    private Double price;

    private Integer limitTime;

    public CourseEnrollResponse(Course course) {
        this.name = course.getName();
        this.id = course.getId();
        this.status = course.getStatus().name();
        this.image = course.getImage();
        this.description = course.getDescription();
        this.createDate = course.getCreateDate();
        this.price = course.getPrice();
        this.limitTime = course.getLimitTime();
    }
}
