package fpt.fall23.onlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.resultdetail.ResultDetailView;

@Entity
@Table(name = "result_quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(ResultQuizView.class)
public class ResultQuiz {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({ ResultDetailView.class, ResultQuizView.class })
	private Long id;

	private LocalDateTime startTime;

	private LocalDateTime submitTime;

	private String resultStatus;

	private Double lastPoint;

	private Integer processTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "enroll_id")
	private Enroll enroll;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quiz_id")
	private Quiz quiz;

	@Column(name = "is_count")
	private Boolean isCount;

	@OneToMany(mappedBy = "resultQuiz", fetch = FetchType.EAGER)
	private Set<ResultDetail> resultDetails;

}
