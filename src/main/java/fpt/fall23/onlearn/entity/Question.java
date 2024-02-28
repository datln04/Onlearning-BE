package fpt.fall23.onlearn.entity;


import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.question.QuestionView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(QuestionView.class)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    private Set<Answer> answers;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}
