package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.dto.syllabus.SyllabusView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "syllabus")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonView(SyllabusView.class)
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ LessonView.class, SyllabusView.class, FeedbackView.class })
    private Long id;

    @JsonView({ LessonView.class, SyllabusView.class, EnrollView.class })
    private String name;

    private String status;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("id asc")
    private Set<Lesson> lessons;

}
