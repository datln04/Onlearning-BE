package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.entity.Syllabus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollRepository extends JpaRepository<Enroll, Long> {

    List<Enroll> findEnrollByCourseId(Long courseId);

    List<Enroll> findEnrollByStudentId(Long studentId);


    Enroll findEnrollByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollStatus enrollStatus);

    @Query(value = "select e.* from Enroll e where e.student_id = :studentId and e.course_id = :courseId and e.status in ('PROCESSING', 'DONE') LIMIT 1", nativeQuery = true)
    Enroll findEnrollByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId);

    @Query(value = "select e.* from Enroll e where e.status in ('DONE')", nativeQuery = true)
    List<Enroll> getAllEnrollByStatusDone();

    @Query(value = "SELECT COUNT(*) FROM enroll WHERE MONTH(request_date) = :month and status in ('PROCESSING', 'DONE')", nativeQuery = true)
    Integer totalEnrolledByMonth(@Param("month") int month);

    Enroll getEnrollById(Long id);

    List<Enroll> findEnrollsByCourseIdAndStatus(Long courseId, EnrollStatus enrollStatus);


    @Query(value = "select count(e.id) from enroll e where e.student_id = :studentId and e.status = 'REFUNDED' and e.course_id = :courseId", nativeQuery = true)
    Integer countTotalEnrollRefunded(Long studentId, Long courseId);


    @Query(value = "select s from Enroll e left join e.syllabus s where e.id = :enrollId")
    Syllabus findSyllabusByEnroll(Long enrollId);
}
