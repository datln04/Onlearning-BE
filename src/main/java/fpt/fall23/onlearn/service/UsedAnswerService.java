package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.UsedAnswer;

import java.util.List;
import java.util.Optional;


public interface UsedAnswerService{
    List<UsedAnswer> findAllByUsedQuestionId(Long usedQuestionId);
    List<UsedAnswer> findAllUsedAnswers();

    Optional<UsedAnswer> getUsedAnswerById(Long id);

    UsedAnswer saveUsedAnswer(UsedAnswer usedAnswer);
    List<UsedAnswer> saveListUsedAnswer(List<UsedAnswer> usedAnswer);

    UsedAnswer checkUsedAnswer(Long usedAnswerId);



}
