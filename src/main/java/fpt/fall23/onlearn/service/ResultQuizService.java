package fpt.fall23.onlearn.service;

import java.util.List;


import fpt.fall23.onlearn.entity.ResultQuiz;

public interface ResultQuizService {

	ResultQuiz saveResultQuiz(ResultQuiz resultQuiz);

	List<ResultQuiz> findResultQuizByQuizId(Long quizId);

	ResultQuiz findResultQuizById(Long id);

	Integer countResultQuizByQuizIdAndStudentId(Long quizId, Long studentId);


	List<ResultQuiz> findResultQuizByStudentIdAndQuizId(Long studentId, Long quizId);


	List<ResultQuiz> findResultQuizByStudentIdAndCourseId(Long studentId, Long courseId);


}
