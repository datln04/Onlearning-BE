package fpt.fall23.onlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fall23.onlearn.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
	
	@Query(value = "select f from Feedback f" +
		    " left join f.enroll e" +
		    " left join e.course c" +
		    " where c.id = :courseId")
	List<Feedback> getFeedbackByEnrollCourseId(@Param("courseId") Long courseId);

	@Query(value = "select f from Feedback f" +
			" left join f.enroll e" +
			" left join e.course c" +
			" where e.student.id = :studentId")
	List<Feedback> getFeedbackByEnrollStudentId(@Param("studentId") Long studentId);

	@Query(value = "select f from Feedback f" +
			" left join f.enroll e" +
			" left join e.course c" +
			" where e.student.id = :studentId and e.course.id = :courseId")
	List<Feedback> getFeedbackByEnrollCourseStudent(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

	Feedback getFeedbackByEnrollId(Long enrollId);
	
}
