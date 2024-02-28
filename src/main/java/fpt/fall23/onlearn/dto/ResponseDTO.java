package fpt.fall23.onlearn.dto;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.dto.subject.SubjectView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonView({CourseView.class, SubjectView.class, TransactionView.class,WithdrawRequestView.class, ReportView.class,
        ResultQuizView.class, ViewUsedQuestionDoQuiz.class,
        FeedbackView.class})
public class ResponseDTO<T> {
    private int code;
    private String message;
    private T responseObject;
}
