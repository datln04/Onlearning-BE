package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findAllQuestionByTeacherId(Long teacherId);
    
    List<Question> findAllQuestionByLessonId(Long lessonId);

    List<Question> findAllQuestions();

    Question saveQuestion(Question question);

    Question getQuestionById(Long id);


}
