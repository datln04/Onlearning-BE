package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Question;
import fpt.fall23.onlearn.repository.QuestionRepository;
import fpt.fall23.onlearn.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public List<Question> findAllQuestionByTeacherId(Long teacherId) {
        return questionRepository.findAllByTeacherId(teacherId);
    }

    @Override
    public List<Question> findAllQuestionByLessonId(Long lessonId) {
        return questionRepository.findAllByLessonId(lessonId);
    }

    @Override
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question getQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        }
        return null;
    }
}
