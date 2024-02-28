package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

    List<Syllabus> getSyllabusByCourseId(Long courseId);
    Syllabus getSyllabusById(Long syllabusId);

    @Query("SELECT s FROM Syllabus s JOIN s.lessons l WHERE l.id = :lessonId")
    List<Syllabus> findSyllabusesByLessonId(Long lessonId);

    @Query("SELECT s from Syllabus s JOIN s.lessons l where s.course.id = :courseId and s.status like :status")
    Syllabus getSyllabusByCourseAndStatus(Long courseId, String status);

}
