package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.UsedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedQuestionRepository extends JpaRepository<UsedQuestion, Long> {
    @Query("select u from UsedQuestion  u join u.quiz q where q.id = :quizId and u.status = true")
    List<UsedQuestion> findAllByQuizId(Long quizId);


}
