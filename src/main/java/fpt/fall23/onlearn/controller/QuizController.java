package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.quiz.DoQuizDetailRequest;
import fpt.fall23.onlearn.dto.quiz.DoQuizRequest;
import fpt.fall23.onlearn.dto.quiz.QuizRequest;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.dto.quiz.ViewQuiz;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.CourseStatus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.repository.CourseRepository;
import fpt.fall23.onlearn.repository.QuizRepository;
import fpt.fall23.onlearn.repository.ResultQuizRepository;
import fpt.fall23.onlearn.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @GetMapping("/byLesson")
    @JsonView(ViewQuiz.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Quiz>> findAllQuizByLessonId(@RequestParam(name = "lesson_id") Long lessonId,
                                                            @RequestParam(name = "status", defaultValue = "") String status) {
        return new ResponseEntity<>(quizService.findAllQuizByLessonId(lessonId, status), HttpStatus.OK);
    }

    @GetMapping("/quizzes")
    @JsonView(ViewQuiz.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Quiz>> findAllQuizes() {
        return new ResponseEntity<>(quizService.findAllQuiz(), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @JsonView(ViewQuiz.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Quiz> findQuizById(@RequestParam(name = "quiz_id") Long quizId) {
        Optional<Quiz> quiz = quizService.getQuizById(quizId);
        return quiz.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @Autowired
    LessonService lessonService;

    @PostMapping("/save")
    @JsonView(ViewQuiz.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Quiz> saveQuiz(@RequestBody QuizRequest quizRequest) {
        Quiz quiz = new Quiz();
        BeanUtils.copyProperties(quizRequest, quiz);
        quiz.setDateCreate(LocalDate.now());
        Lesson lesson = lessonService.getLessonById(quizRequest.getLessonId());
        quiz.setLesson(lesson);
        return new ResponseEntity<>(quizService.saveQuiz(quiz), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Object> deleteQuiz(@RequestParam(name = "quizId") Long quizId) {
        Boolean result = quizService.removeQuiz(quizId);
        return new ResponseEntity<>(result ? "Remove success" : "Remove fail", HttpStatus.OK);
    }

    @DeleteMapping("/disable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Quiz> disableQuiz(@RequestParam(name = "quizId") Long quizId) {
        return quizService.disableQuiz(quizId);
    }

    @GetMapping("/enable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Quiz> enableQuiz(@RequestParam(name = "quizId") Long quizId) {
        return quizService.enableQuiz(quizId);
    }

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

    @Autowired
    QuizRepository quizRepository;


    @PostMapping("/do-quiz")
    @JsonView(ResultQuizView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<ResultQuiz> doQuiz(@RequestBody DoQuizRequest doQuizRequest) throws ParseException {
        return quizService.doQuiz(doQuizRequest);
    }


    @GetMapping("/attempt-time")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Integer>> attemptTime(@RequestParam(name = "quizId") Long quizId, @RequestParam("studentId") Long studentId) {
        return new ResponseEntity<>(quizService.getAttempTimeByQuizIdAndStudentId(quizId, studentId), HttpStatus.OK);
    }


}
