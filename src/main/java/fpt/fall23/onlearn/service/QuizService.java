package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.quiz.DoQuizRequest;
import fpt.fall23.onlearn.entity.Quiz;
import fpt.fall23.onlearn.entity.ResultQuiz;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface QuizService {
    List<Quiz> findAllQuizByLessonId(Long lessonId, String status);

    List<Quiz> findAllQuiz();

    Optional<Quiz> getQuizById(Long id);

    Quiz saveQuiz(Quiz quiz);

    Boolean removeQuiz(Long id);

    ResponseDTO<Quiz> disableQuiz(Long quizId);

    ResponseDTO<Quiz> enableQuiz(Long quizId);


    ResponseDTO<Integer> getAttempTimeByQuizIdAndStudentId(Long quizId, Long studentId);

    ResponseDTO<ResultQuiz> doQuiz(DoQuizRequest doQuizRequest) throws ParseException;

}

