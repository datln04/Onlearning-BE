package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l JOIN l.course c where c.id = :courseId order by l.id asc")
    List<Lesson> findLessonByCourseId(Long courseId);

    @Query("SELECT l FROM Lesson l JOIN l.syllabuses s WHERE s.id = :syllabusId order by l.id asc")
    List<Lesson> findLessonsBySyllabusId(@Param("syllabusId") Long syllabusId);
}
