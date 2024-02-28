package fpt.fall23.onlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.fall23.onlearn.entity.FeedbackDetail;

@Repository
public interface FeedbackDetailRepository extends JpaRepository<FeedbackDetail,Long>{

	List<FeedbackDetail> findAllByFeedbackId(Long feedbackId);
	
}
