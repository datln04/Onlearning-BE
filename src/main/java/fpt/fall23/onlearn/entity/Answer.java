package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.question.QuestionView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(QuestionView.class)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Boolean isCorrect;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "account_id")
//    private Account account;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "course_id")
//    private Course course;


}
