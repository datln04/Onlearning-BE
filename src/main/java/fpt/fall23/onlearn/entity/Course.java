package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.dto.question.QuestionView;
import fpt.fall23.onlearn.dto.rejectcourse.RejectCourseView;
import fpt.fall23.onlearn.dto.subject.SubjectView;
import fpt.fall23.onlearn.dto.syllabus.SyllabusView;
import fpt.fall23.onlearn.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonView(CourseView.class)
public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonView({ EnrollView.class, LessonView.class, SyllabusView.class, CourseView.class, SubjectView.class,
                        QuestionView.class,
                        RejectCourseView.class, FeedbackView.class })
        private Long id;

        @JsonView({ EnrollView.class, LessonView.class, SyllabusView.class, CourseView.class, SubjectView.class,
                        QuestionView.class,
                        RejectCourseView.class, FeedbackView.class })
        private String name;

        @Enumerated(EnumType.STRING)
        private CourseStatus status;

        private String image;

        @Column(columnDefinition = "TEXT")
        private String description;

        private LocalDateTime createDate;

        private Double price;

        private Integer limitTime;

        private Double averagePoint;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "teacher_id")
        private Teacher teacher;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "subject_id")
        private Subject subject;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
        @JsonIgnore
        private Set<Lesson> lessons;

}
