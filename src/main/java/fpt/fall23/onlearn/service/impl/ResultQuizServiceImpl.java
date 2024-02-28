package fpt.fall23.onlearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.entity.ResultQuiz;
import fpt.fall23.onlearn.repository.ResultQuizRepository;
import fpt.fall23.onlearn.service.ResultQuizService;

@Service
public class ResultQuizServiceImpl implements ResultQuizService {

    @Autowired
    ResultQuizRepository repository;

    @Override
    public ResultQuiz saveResultQuiz(ResultQuiz resultQuiz) {
        return repository.save(resultQuiz);
    }

    @Override
    public List<ResultQuiz> findResultQuizByQuizId(Long quizId) {
        return repository.findResultQuizsByQuizId(quizId);
    }

    @Override
    public ResultQuiz findResultQuizById(Long id) {
        Optional<ResultQuiz> resultQuiz = repository.findById(id);
        if (resultQuiz.isPresent()) {
            return resultQuiz.get();
        }
        return null;
    }

    @Override
    public Integer countResultQuizByQuizIdAndStudentId(Long quizId, Long studentId) {
        return repository.countAllByQuizIdAndStudentId(quizId, studentId);
    }

    @Override
    public List<ResultQuiz> findResultQuizByStudentIdAndQuizId(Long studentId, Long quizId) {
        return repository.findResultQuizsByStudentIdAndQuizIdAndStatus(studentId, quizId);
    }

    @Override
    public List<ResultQuiz> findResultQuizByStudentIdAndCourseId(Long studentId, Long courseId) {
        return repository.findResultQuizByStudentIdAndCourseId(studentId, courseId);
    }
}
