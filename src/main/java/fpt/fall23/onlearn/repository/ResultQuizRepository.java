package fpt.fall23.onlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fpt.fall23.onlearn.entity.ResultQuiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ResultQuizRepository extends JpaRepository<ResultQuiz, Long> {
    List<ResultQuiz> findResultQuizsByQuizId(Long quizId);
    @Query(value = "select rq from ResultQuiz rq where rq.quiz.id = :quizId and rq.student.id = :studentId")
    List<ResultQuiz> findResultQuizsByStudentIdAndQuizId(Long studentId, Long quizId);

    @Query(value = "select rq from ResultQuiz rq where rq.quiz.id = :quizId and rq.student.id = :studentId  and rq.isCount = true")
    List<ResultQuiz> findResultQuizsByStudentIdAndQuizIdAndStatus(Long studentId, Long quizId);

    @Query(value = "select count(rq.id) from ResultQuiz rq where rq.quiz.id = :quizId and rq.student.id = :studentId and rq.isCount = true")
    Integer countAllByQuizIdAndStudentId(Long quizId, Long studentId);


    @Query(nativeQuery = true,
            value = "\n" +
                    "select rq.* from enroll e \n" +
                    "left join result_quiz rq on e.id = rq.enroll_id \n" +
                    "where e.student_id = :studentId and e.course_id = :courseId " +
                    "ORDER BY rq.submit_time desc \n")
    List<ResultQuiz> findResultQuizByStudentIdAndCourseId(Long studentId, Long courseId);


    @Query(value = "SELECT * FROM result_quiz rq WHERE rq.student_id = :studentId AND rq.quiz_id = :quizId AND rq.result_status = 'PASS' order by rq.id desc LIMIT 1 ", nativeQuery = true)
    ResultQuiz findPassResultQuiz(@Param("studentId") Long studentId, @Param("quizId") Long quizId);

    @Query(value = "SELECT * FROM result_quiz rq WHERE rq.student_id = :studentId AND rq.quiz_id = :quizId order by rq.id desc LIMIT 1 ", nativeQuery = true)
    ResultQuiz findLastResultQuiz(@Param("studentId") Long studentId, @Param("quizId") Long quizId);

}
