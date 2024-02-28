package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Teacher;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findTeacherByAccountId(Long accountId);

//	@Query(value="select t from Teacher t join Course c where c.id = :courseId")
//	Teacher findTeacherByCourseId(long courseId);

}
