package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.resultdetail.ResultDetailView;
import fpt.fall23.onlearn.dto.used.question.ViewUsedQuestion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "used_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(ViewUsedQuestion.class)
public class UsedAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ResultDetailView.class, ViewUsedQuestion.class, ViewUsedQuestionDoQuiz.class,ResultQuizView .class})
    private Long id;

    @JsonView({ResultDetailView.class, ViewUsedQuestion.class, ViewUsedQuestionDoQuiz.class,ResultQuizView .class})
    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonView({ResultDetailView.class, ViewUsedQuestion.class})
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "used_question_id")
    @JsonIgnore
    private UsedQuestion usedQuestion;

}
