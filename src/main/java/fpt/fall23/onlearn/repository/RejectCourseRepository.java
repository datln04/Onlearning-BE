package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.RejectCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RejectCourseRepository extends JpaRepository<RejectCourse, Long> {

    List<RejectCourse> findAllByCourseId(Long courseId);

}
