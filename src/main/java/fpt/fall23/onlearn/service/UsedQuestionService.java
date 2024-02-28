package fpt.fall23.onlearn.service;


import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.ResponseList;
import fpt.fall23.onlearn.entity.UsedQuestion;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UsedQuestionService {
    List<UsedQuestion> getAllUsedQuestionByQuizId(Long quizId);

    ResponseDTO<String> checkCanDoQuiz(Long quizId);


    List<UsedQuestion> findAllUsedQuestionByQuizId(Long quizId);

    List<UsedQuestion> findAllUsedQuestion();

    Optional<UsedQuestion> getUsedQuestionById(Long id);

    UsedQuestion saveUsedQuestion(UsedQuestion usedQuestion);

    ResponseDTO<UsedQuestion> updateUsedQuestion(Long usedQuestionId, Boolean status);


}
