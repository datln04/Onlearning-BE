package fpt.fall23.onlearn.entity;

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

import java.util.Set;

@Entity
@Table(name = "used_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({ViewUsedQuestion.class,ViewUsedQuestionDoQuiz.class})
public class UsedQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ResultDetailView.class,ViewUsedQuestion.class, ViewUsedQuestionDoQuiz.class,ResultQuizView.class})
    private Long id;
    @JsonView({ResultDetailView.class,ViewUsedQuestion.class, ViewUsedQuestionDoQuiz.class,ResultQuizView.class})
    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usedQuestion")
    private Set<UsedAnswer> usedAnswers;


}
