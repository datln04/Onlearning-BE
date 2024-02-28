package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Answer;
import fpt.fall23.onlearn.repository.AnswerRepository;
import fpt.fall23.onlearn.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
	@Autowired
	AnswerRepository answerRepository;

	@Override
	public List<Answer> findAllAnswerByQuestionId(Long questionId) {
		return answerRepository.findAllByQuestionId(questionId);
	}

	@Override
	public List<Answer> listAllAnswers() {
		return answerRepository.findAll();
	}

	@Override
	public Answer saveAnswer(Answer answer) {
		return answerRepository.save(answer);
	}

	@Override
	public Answer getAnswerById(Long id) {
		Optional<Answer> answer = answerRepository.findById(id);
		if (answer.isPresent()) {
			return answer.get();
		}
		return null;
	}
}
