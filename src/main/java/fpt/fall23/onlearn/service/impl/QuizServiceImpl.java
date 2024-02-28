package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.quiz.DoQuizDetailRequest;
import fpt.fall23.onlearn.dto.quiz.DoQuizRequest;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.repository.*;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.DateTimeUtils;
import jakarta.persistence.EntityManager;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {


    @Autowired
    QuizRepository quizRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    UsedQuestionService usedQuestionService;

    @Autowired
    UsedAnswerService usedAnswerService;

    @Autowired
    ResultQuizService resultQuizService;

    @Autowired
    ResultQuizRepository resultQuizRepository;


    @Autowired
    ResultDetailService resultDetailService;

    @Autowired
    StudentService studentService;

    @Autowired
    EnrollService enrollService;

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<Quiz> findAllQuizByLessonId(Long lessonId, String status) {
        return quizRepository.findAllByLessonId(lessonId, "%" + status + "%");
    }

    @Override
    public List<Quiz> findAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    @Override
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UsedQuestionRepository usedQuestionRepository;

    @Override
    public Boolean removeQuiz(Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);

        if (quiz.isEmpty()) {
            return false;
        } else {
            List<UsedQuestion> usedQuestions = usedQuestionRepository.findAllByQuizId(quiz.get().getId());
            if (usedQuestions != null && usedQuestions.size() > 0) {
                usedQuestions.forEach(e -> {
                    usedQuestionRepository.deleteById(e.getId());
                });
            }
            quizRepository.deleteById(quiz.get().getId());
        }
        return true;
    }

    @Override
    public ResponseDTO<Integer> getAttempTimeByQuizIdAndStudentId(Long quizId, Long studentId) {

        String nativeQuery = "SELECT COUNT(*) FROM result_quiz rq WHERE rq.student_id = ? AND rq.quiz_id = ? AND rq.is_count = true";
        Long result = (Long) entityManager.createNativeQuery(nativeQuery)
                .setParameter(1, studentId)
                .setParameter(2, quizId)
                .getSingleResult();
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", Integer.valueOf(result.toString()));
    }

    @Override
    @Transactional
    public ResponseDTO<Quiz> disableQuiz(Long quizId) {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz != null) {
            quiz.setStatus("Deactive");
            List<UsedQuestion> usedQuestions = usedQuestionRepository.findAllByQuizId(quizId);
            for (UsedQuestion usedQuestion :
                    usedQuestions) {
                usedQuestion.setStatus(false);
                usedQuestionRepository.save(usedQuestion);
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), "success", quiz);
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "not found quiz", null);
    }

    @Override
    @Transactional
    public ResponseDTO<Quiz> enableQuiz(Long quizId) {
        Quiz quiz = quizRepository.getQuizById(quizId);
        if (quiz != null) {
            quiz.setStatus("Active");
            List<UsedQuestion> usedQuestions = usedQuestionRepository.findAllByQuizId(quizId);
            for (UsedQuestion usedQuestion :
                    usedQuestions) {
                usedQuestion.setStatus(true);
                usedQuestionRepository.save(usedQuestion);
            }
            return new ResponseDTO<>(HttpStatus.OK.value(), "success", quiz);
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "not found quiz", null);
    }


    @Override
    @Transactional
    public ResponseDTO<ResultQuiz> doQuiz(DoQuizRequest doQuizRequest) throws ParseException {
        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();

        Optional<Quiz> quizOpt = getQuizById(doQuizRequest.getQuizId());
        if (quizOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy quiz", null);

        }
        Quiz quiz = quizOpt.get();

        Enroll enroll = enrollService.getEnrollById(doQuizRequest.getEnrollId());
        Student student = enroll.getStudent();

        Integer allowAttempt = quiz.getAllowAttempt();
        Integer attempt = resultQuizService.countResultQuizByQuizIdAndStudentId(quiz.getId(), student.getId());
        attempt = attempt == null ? 0 : attempt;
        if (attempt >= allowAttempt) {
            ResultQuiz resultQuiz = resultQuizRepository.findPassResultQuiz(student.getId(), quiz.getId());
            if (resultQuiz == null) { //not pass
                List<ResultQuiz> resultQuizs = resultQuizRepository.findResultQuizsByStudentIdAndQuizId(student.getId(), quiz.getId());
                for (ResultQuiz rq :
                        resultQuizs) {
                    rq.setIsCount(false);
                    resultQuizRepository.save(rq);
                }
            }
        }

        int correctCount = 0;
        ResultQuiz resultQuiz = resultQuizService.saveResultQuiz(new ResultQuiz());

        int totalQuestion = doQuizRequest.getDoQuizDetailRequests().size();
        for (DoQuizDetailRequest detail : doQuizRequest.getDoQuizDetailRequests()) {
            Optional<UsedQuestion> usedQuestionOpt = usedQuestionService.getUsedQuestionById(detail.getQuestionId());
            if (usedQuestionOpt.isPresent()) {
                UsedQuestion usedQuestion = usedQuestionOpt.get();
                for (Long choiceId : detail.getAnswerIds()) {
                    ResultDetail resultDetail = new ResultDetail();
                    Boolean isCorrect = false;

                    List<UsedAnswer> usedAnswers = usedQuestion.getUsedAnswers().stream().toList();
                    for (UsedAnswer answer : usedAnswers) {
                        if (answer.getId().equals(choiceId)) {
                            if (answer.getIsCorrect() == true) {
                                correctCount++;
                                isCorrect = true;
                            }
                        }
                    }

                    resultDetail.setResultQuiz(resultQuiz);
                    Optional<UsedAnswer> userAnswer = usedAnswerService.getUsedAnswerById(choiceId);
                    if (userAnswer.isPresent()) {
                        resultDetail.setUsedAnswer(userAnswer.get());
                    }

                    resultDetail.setIsCorrect(isCorrect);
                    resultDetail.setUsedQuestion(usedQuestion);
                    resultDetailService.saveResultDetail(resultDetail);
                }
            } // else - handle case where usedQuestionOpt is empty (not shown in the provided code)
        }
        resultQuiz.setStartTime(DateTimeUtils.convertStringToLocalDate(doQuizRequest.getStartTime()));
        resultQuiz.setSubmitTime(LocalDateTime.now());
        resultQuiz.setQuiz(quiz);
        resultQuiz.setStudent(student);
        double calculatedPoint = (10.0 / totalQuestion) * correctCount;
        String formattedPoint = String.format("%.2f", calculatedPoint);
        resultQuiz.setLastPoint(Double.parseDouble(formattedPoint));
//        resultQuiz.setLastPoint(correctCount);
        resultQuiz.setEnroll(enroll);
        resultQuiz.setIsCount(true);
        resultQuiz.setProcessTime(attempt + 1);
//        resultQuiz.setProcessTime(countChoice);
        if (resultQuiz.getLastPoint() >= quiz.getPassScore()) {
            resultQuiz.setResultStatus("PASS");
        } else {
            resultQuiz.setResultStatus("FAIL");
        }
        List<ResultDetail> resultDetails = resultDetailService.findResultDetailByResultQuizId(resultQuiz.getId());
        if (resultDetails != null) {
            resultQuiz.setResultDetails(resultDetails.stream().collect(Collectors.toSet()));
        }

        resultQuiz = resultQuizService.saveResultQuiz(resultQuiz);
        Course course = courseRepository.findCourseByQuiz(quiz.getId());
        if (course != null) {
            Double point = 0.0;
            Boolean isPassAll = true;
            List<Quiz> quizList = quizRepository.findAllQuizByCourse(course.getId());
            ResultQuiz passResultQuiz = null;
            for (Quiz q :
                    quizList) {
                passResultQuiz = resultQuizRepository.findPassResultQuiz(student.getId(), q.getId());
                if (passResultQuiz != null) {
                    point += passResultQuiz.getLastPoint();
                } else {
                    isPassAll = false;
                    break;
                }

            }
            if (isPassAll) {
                Double averagePoint = point / quizList.size();
                if (averagePoint >= course.getAveragePoint()) {
                    enroll.setStatus(EnrollStatus.DONE);
                    enrollService.saveEnroll(enroll);
                    return new ResponseDTO<>(HttpStatus.OK.value(), "Nộp bài thành công và hoàn tất khoá học", resultQuiz);
                } else {
                    return new ResponseDTO<>(HttpStatus.OK.value(), "Nộp bài thành công và đểm chưa đủ để hoàn tất", resultQuiz);
                }
            }
        }

        return new ResponseDTO<>(HttpStatus.OK.value(), "Nộp bài thành công", resultQuiz);
    }
}
