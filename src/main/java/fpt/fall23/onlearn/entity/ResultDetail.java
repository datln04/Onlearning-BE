package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.resultdetail.ResultDetailView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "result_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(ResultDetailView.class)
public class ResultDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({ ResultQuizView.class, ResultDetailView.class })
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "result_quiz_id")

	private ResultQuiz resultQuiz;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "used_question_id")
	@JsonView({ResultQuizView .class, ResultDetailView.class})
	private UsedQuestion usedQuestion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "used_answer_id")
	@JsonView({ResultQuizView .class, ResultDetailView.class})
	private UsedAnswer usedAnswer;

	@Column(name = "is_correct")
	private Boolean isCorrect;

}
