package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.UsedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedAnswerRepository extends JpaRepository<UsedAnswer, Long> {
    @Query("select u from UsedAnswer u join u.usedQuestion q where q.id = :usedQuestionId")
    List<UsedAnswer> findAllByUsedQuestionId(Long usedQuestionId);


    @Query("select u from UsedAnswer u where u.isCorrect = true and u.id = :usedAnswerId")
    UsedAnswer checkUsedAnswerById(Long usedAnswerId);
}
