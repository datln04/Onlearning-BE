package fpt.fall23.onlearn.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(FeedbackView.class)
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String content;

	private Boolean status;

	private LocalDate createDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enroll_id")
	private Enroll enroll;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
	private Set<FeedbackDetail> feedbackDetails;

}
