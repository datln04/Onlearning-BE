package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.ResponseList;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.repository.*;
import fpt.fall23.onlearn.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsedQuestionServiceImpl implements UsedQuestionService {
    @Autowired
    UsedQuestionRepository usedQuestionRepository;

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    EnrollService enrollService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ResultQuizService resultQuizService;

    @Autowired
    ResultQuizRepository resultQuizRepository;

    @Autowired
    EnrollRepository enrollRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<UsedQuestion> getAllUsedQuestionByQuizId(Long quizId) {
//        Optional<Account> currentAccountOpt = authenticationService.getCurrentAuthenticatedAccount();
//        if (currentAccountOpt.isEmpty()) {
//            return new ResponseList(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy user", null);
////            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//        Student student = studentService.findStudentByAccountId(currentAccountOpt.get().getId());
//
//        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
//
//        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
//        if (quizOpt.isEmpty()) {
//            return new ResponseList(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy quiz", null);
//        }
//        Quiz quiz = quizOpt.get();
//        Course course = courseRepository.findCourseByQuiz(quizId);
//        enrollRepository.findEnrollByStudentIdAndCourseIdAndStatus(student.getId(), course.getId(), EnrollStatus.PROCESSING);
//        Enroll enroll = enrollRepository.findEnrollByStudentIdAndCourseIdAndStatus(student.getId(), course.getId(), EnrollStatus.PROCESSING);
//        Integer allowAttempt = quiz.getAllowAttempt();
//        Integer attempt = resultQuizService.countResultQuizByQuizIdAndStudentId(quiz.getId(), student.getId());
//        attempt = attempt == null ? 0 : attempt;
//        if (attempt >= allowAttempt) {
//            //check quiz id pass or not if pass ->
//            //if not pass check the below
//            //allow attempt check more process time !
//            //getLast processTime + 8;
//            ResultQuiz resultQuiz = resultQuizRepository.findPassResultQuiz(student.getId(), quiz.getId());
//            if (resultQuiz == null) { //not pass
//
//                ResultQuiz lastResutl = resultQuizRepository.findLastResultQuiz(student.getId(), quiz.getId());
//
//                Date submitTime = lastResutl.getSubmitTime();
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(submitTime);
////                calendar.add(Calendar.HOUR_OF_DAY, 8);
//                calendar.add(Calendar.MINUTE, systemConfig.getWaitingQuizTime());
//
//                Date updatedTime = calendar.getTime();
//                Date currentDateTime = new Date();
//                if (currentDateTime.before(updatedTime)) {
//                    return new ResponseList(HttpStatus.BAD_REQUEST.value(), String.format("Cần đợi %s phút để làm tiếp", systemConfig.getWaitingQuizTime()), null);
//                }else{
//                    return new ResponseList(HttpStatus.OK.value(), "success", usedQuestionRepository.findAllByQuizId(quizId));
//                }
//            } else {
//                return new ResponseList(HttpStatus.BAD_REQUEST.value(), "Quá số lần làm bài", null);
//            }
//        }
        return usedQuestionRepository.findAllByQuizId(quizId);
    }

    @Override
    public ResponseDTO<String> checkCanDoQuiz(Long quizId) {
        Optional<Account> currentAccountOpt = authenticationService.getCurrentAuthenticatedAccount();
        if (currentAccountOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy user", null);
        }
        Student student = studentService.findStudentByAccountId(currentAccountOpt.get().getId());

        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();

        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy quiz", null);
        }
        Quiz quiz = quizOpt.get();
        Course course = courseRepository.findCourseByQuiz(quizId);
        enrollRepository.findEnrollByStudentIdAndCourseIdAndStatus(student.getId(), course.getId(), EnrollStatus.PROCESSING);
        Enroll enroll = enrollRepository.findEnrollByStudentIdAndCourseIdAndStatus(student.getId(), course.getId(), EnrollStatus.PROCESSING);
        Integer allowAttempt = quiz.getAllowAttempt();
        Integer attempt = resultQuizService.countResultQuizByQuizIdAndStudentId(quiz.getId(), student.getId());
        attempt = attempt == null ? 0 : attempt;
        if (attempt >= allowAttempt) {
            ResultQuiz resultQuiz = resultQuizRepository.findPassResultQuiz(student.getId(), quiz.getId());
            if (resultQuiz == null) { //not pass
                ResultQuiz lastResult = resultQuizRepository.findLastResultQuiz(student.getId(), quiz.getId());
                LocalDateTime submitTime = lastResult.getSubmitTime(); // Assuming submitTime is already a LocalDate

                // Calculate the time when the user can take the quiz again
                LocalDateTime nextAvailableTime = submitTime.plusMinutes(systemConfig.getWaitingQuizTime());

                LocalDateTime currentTime = LocalDateTime.now();

                if (currentTime.isBefore(nextAvailableTime)) {
                    // Calculate the remaining time in minutes
                    long remainingSeconds = java.time.Duration.between(currentTime, nextAvailableTime).getSeconds();
                    long remainingMinutes = remainingSeconds / 60;
                    remainingSeconds = remainingSeconds % 60;

                    // Format the remaining time as MM:SS
                    String formattedTime = String.format("%02d:%02d", remainingMinutes, remainingSeconds);

                    // Display the remaining time to the user
                    return new ResponseDTO<>(
                            HttpStatus.BAD_REQUEST.value(),
                            String.format("Cần đợi %s để làm tiếp", formattedTime),
                            null
                    );
                }
            } else {
                return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Quá số lần làm bài", null);
            }
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", "Được làm quiz");
    }

    @Override
    public List<UsedQuestion> findAllUsedQuestionByQuizId(Long quizId) {
        return usedQuestionRepository.findAllByQuizId(quizId);
    }

    @Override
    public List<UsedQuestion> findAllUsedQuestion() {
        return usedQuestionRepository.findAll();
    }

    @Override
    public Optional<UsedQuestion> getUsedQuestionById(Long id) {
        return usedQuestionRepository.findById(id);
    }

    @Override
    public UsedQuestion saveUsedQuestion(UsedQuestion usedQuestion) {
        return usedQuestionRepository.save(usedQuestion);
    }

    @Override
    public ResponseDTO<UsedQuestion> updateUsedQuestion(Long usedQuestionId, Boolean status) {
        Optional<UsedQuestion> usedQuestionOpt = usedQuestionRepository.findById(usedQuestionId);
        if (usedQuestionOpt.isPresent()) {
            UsedQuestion usedQuestion = usedQuestionOpt.get();
            usedQuestion.setStatus(status);
            usedQuestion = usedQuestionRepository.save(usedQuestion);
            return new ResponseDTO<>(HttpStatus.OK.value(), "success", usedQuestion);
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "not found used question", null);
    }
}
