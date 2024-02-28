package fpt.fall23.onlearn.repository;


import fpt.fall23.onlearn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query(value = "select q from Quiz q where q.lesson.id = :lessonId and q.status like :status")
    List<Quiz> findAllByLessonId(Long lessonId, String status);

    Quiz getQuizById(Long id);

    @Query(nativeQuery = true,
    value = "select q.* from quiz q\n" +
            "left join lesson l on q.lesson_id = l.id\n" +
            "left join course c on l.course_id = c.id\n" +
            "where c.id = :courseId\n" +
            "order by q.id desc limit 1")
    Quiz findLastQuizByCourseId(Long courseId);

    @Query(nativeQuery = true,
            value = "select q.* from quiz q\n" +
                    "left join lesson l on q.lesson_id = l.id\n" +
                    "left join course c on l.course_id = c.id\n" +
                    "where c.id = :courseId")
    List<Quiz> findAllQuizByCourse(Long courseId);


}
