package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.dto.question.QuestionView;
import fpt.fall23.onlearn.dto.quiz.ViewQuiz;
import fpt.fall23.onlearn.dto.syllabus.SyllabusView;
import fpt.fall23.onlearn.enums.LessonType;
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
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonView({ LessonView.class })
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ SyllabusView.class, ViewQuiz.class, LessonView.class, QuestionView.class })
    private Long id;

    @JsonView({ SyllabusView.class, LessonView.class })
    private String name;
    @JsonView({ SyllabusView.class, LessonView.class })
    private String status;
    @JsonView({ SyllabusView.class, LessonView.class })
    @Column(columnDefinition = "TEXT")
    private String description;
    @JsonView({ SyllabusView.class, LessonView.class })
    private String url;
    @JsonView({ SyllabusView.class, LessonView.class })
    private LocalDateTime dateTime;
    @JsonView({ SyllabusView.class, LessonView.class })
    private Integer estimateTime;
    @JsonView({ SyllabusView.class, LessonView.class })
    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonView({ SyllabusView.class, LessonView.class })
    @Enumerated(EnumType.STRING)
    private LessonType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lesson")
    private Set<Resource> resources;

    @ManyToMany()
    @JoinTable(name = "syllabus_lessons", joinColumns = @JoinColumn(name = "lessons_id"), inverseJoinColumns = @JoinColumn(name = "syllabus_id"))
    private Set<Syllabus> syllabuses;

}
