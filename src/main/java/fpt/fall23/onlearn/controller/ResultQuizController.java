package fpt.fall23.onlearn.controller;

import java.util.List;

import fpt.fall23.onlearn.dto.resultdetail.ResultDetailView;
import fpt.fall23.onlearn.entity.ResultDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.quiz.ResultQuizView;
import fpt.fall23.onlearn.entity.ResultQuiz;
import fpt.fall23.onlearn.service.ResultQuizService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/result-quiz")
public class ResultQuizController {

    @Autowired
    ResultQuizService resultQuizService;

    // ResultQuiz saveResultQuiz(ResultQuiz resultQuiz);
    //
    // List<ResultQuiz> findResultQuizByQuizId(Long quizId);
    //
    // ResultQuiz findResultQuizById(Long id);

    @GetMapping(path = "/by-quiz")
    @JsonView(ResultQuizView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<ResultQuiz>> findResultQuizByQuizId(@RequestParam(name = "quiz_id") Long quizId) {
        return new ResponseEntity<>(resultQuizService.findResultQuizByQuizId(quizId), HttpStatus.OK);
    }

    @GetMapping(path = "/by-id")
    @JsonView(ResultQuizView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResultQuiz> findResultQuizById(@RequestParam(name = "id") long id) {
        return new ResponseEntity<ResultQuiz>(resultQuizService.findResultQuizById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/by-student-id")
    @JsonView(ResultQuizView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<ResultQuiz>> findAllByStudentId(
            @RequestParam(name = "student_id") Long student_id,
            @RequestParam(name = "quiz_id") Long quiz_id) {
        return new ResponseEntity<>(resultQuizService.findResultQuizByStudentIdAndQuizId(student_id, quiz_id),
                HttpStatus.OK);
    }

    @GetMapping(path = "/by-student-course")
    @JsonView(ResultQuizView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<ResultQuiz>> findAllByStudentAndCourse(
            @RequestParam(name = "student_id") Long student_id,
            @RequestParam(name = "course_id") Long course_id) {
        return new ResponseEntity<>(resultQuizService.findResultQuizByStudentIdAndCourseId(student_id, course_id),
                HttpStatus.OK);
    }

}
