package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.rejectcourse.RejectCourseView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reject_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(RejectCourseView.class)
public class RejectCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDate dateReject;

    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id")
    private Course course;

}
