package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.UsedAnswer;
import fpt.fall23.onlearn.repository.UsedAnswerRepository;
import fpt.fall23.onlearn.service.UsedAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsedAnswerServiceImpl implements UsedAnswerService {
    @Autowired
    UsedAnswerRepository usedAnswerRepository;

    @Override
    public List<UsedAnswer> findAllByUsedQuestionId(Long usedQuestionId) {
        return usedAnswerRepository.findAllByUsedQuestionId(usedQuestionId);
    }

    @Override
    public List<UsedAnswer> findAllUsedAnswers() {
        return usedAnswerRepository.findAll();
    }

    @Override
    public Optional<UsedAnswer> getUsedAnswerById(Long id) {
        return usedAnswerRepository.findById(id);
    }

    @Override
    public UsedAnswer saveUsedAnswer(UsedAnswer usedAnswer) {
        return usedAnswerRepository.save(usedAnswer);
    }

    @Override
    public List<UsedAnswer> saveListUsedAnswer(List<UsedAnswer> usedAnswers) {
        return usedAnswerRepository.saveAll(usedAnswers);
    }

    @Override
    public UsedAnswer checkUsedAnswer(Long usedAnswerId) {
        return usedAnswerRepository.checkUsedAnswerById(usedAnswerId);
    }
}
