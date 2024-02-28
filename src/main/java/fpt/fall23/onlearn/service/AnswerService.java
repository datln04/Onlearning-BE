package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> findAllAnswerByQuestionId(Long questionId);

    List<Answer> listAllAnswers();

    Answer saveAnswer(Answer answer);

    Answer getAnswerById(Long id);


}
