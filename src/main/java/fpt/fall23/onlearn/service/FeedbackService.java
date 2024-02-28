package fpt.fall23.onlearn.service;

import java.math.BigDecimal;
import java.util.List;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.entity.FeedContent;
import fpt.fall23.onlearn.entity.Feedback;
import fpt.fall23.onlearn.entity.FeedbackDetail;

public interface FeedbackService {

	Feedback getFeedbackByEnrollId(Long enrollId);

	List<Feedback> findAllByCourseId(Long courseId);

	List<Feedback> findAllByStudentId(Long studentId);

	List<Feedback> findAllByCourseStudent(Long courseId);

	List<Feedback> getAllFeedbacks();
	
	Feedback getFeedbackById(Long id);
	
	Feedback saveFeedback(Feedback feedback);
	
	Boolean removeFeedback(Long id);
	
	
	FeedContent saveFeedContent(FeedContent feedContent);
	
	FeedContent getFeedContentById(Long feedContentId);
	
	List<FeedContent> getAllFeedContents();

	FeedbackDetail saveFeedbackDetail(FeedbackDetail feedbackDetail);

	ResponseDTO<BigDecimal> getRatingByTeacherId(Long teacherId);
}
