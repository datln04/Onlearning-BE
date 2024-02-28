package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.ResponseList;
import fpt.fall23.onlearn.dto.question.ViewUsedQuestionDoQuiz;
import fpt.fall23.onlearn.dto.used.question.UsedQuestionRequest;
import fpt.fall23.onlearn.dto.used.question.ViewUsedQuestion;
import fpt.fall23.onlearn.entity.Quiz;
import fpt.fall23.onlearn.entity.UsedAnswer;
import fpt.fall23.onlearn.entity.UsedQuestion;
import fpt.fall23.onlearn.service.AnswerService;
import fpt.fall23.onlearn.service.QuizService;
import fpt.fall23.onlearn.service.UsedAnswerService;
import fpt.fall23.onlearn.service.UsedQuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/used-question")
public class UsedQuestionController {

    @Autowired
    UsedQuestionService usedQuestionService;

    @Autowired
    QuizService quizService;

    @Autowired
    AnswerService answerService;

    @Autowired
    UsedAnswerService usedAnswerService;

    @GetMapping("/by-quiz")
    @JsonView(ViewUsedQuestion.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<UsedQuestion>> findAllUsedQuestionByQuizId(@RequestParam(name = "quiz_id") Long quizId) {
        return new ResponseEntity<>(usedQuestionService.findAllUsedQuestionByQuizId(quizId), HttpStatus.OK);
    }

    @GetMapping("/by-do-quiz")
    @JsonView(ViewUsedQuestionDoQuiz.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<UsedQuestion>> findAllUsedQuestionByDoQuizId(@RequestParam(name = "quiz_id") Long quizId) {
        return new ResponseEntity<>(usedQuestionService.getAllUsedQuestionByQuizId(quizId), HttpStatus.OK);
    }

    @GetMapping("/check-can-do")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<String> checkCanDoQuiz(@RequestParam(name = "quiz_id") Long quizId) {
        return usedQuestionService.checkCanDoQuiz(quizId);
    }

    @GetMapping("/questions")
    @JsonView(ViewUsedQuestion.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<UsedQuestion>> findAllUsedQuestion() {
        return new ResponseEntity<>(usedQuestionService.findAllUsedQuestion(), HttpStatus.OK);
    }

    @GetMapping("/by-id")
    @JsonView(ViewUsedQuestion.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<UsedQuestion> getUsedQuestionById(@RequestParam(name = "question_id") Long id) {
        Optional<UsedQuestion> usedQuestion = usedQuestionService.getUsedQuestionById(id);
        return usedQuestion.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }


    @PostMapping("/save")
    @JsonView(ViewUsedQuestion.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<UsedQuestion> saveUsedQuestion(@RequestBody UsedQuestionRequest usedQuestionRequest) {
        UsedQuestion usedQuestion = new UsedQuestion();
        BeanUtils.copyProperties(usedQuestionRequest, usedQuestion);
        Optional<Quiz> quiz = quizService.getQuizById(usedQuestionRequest.getQuizId());
        usedQuestion.setQuiz(quiz.get());
        usedQuestion = usedQuestionService.saveUsedQuestion(usedQuestion);
        List<Long> answerIds = usedQuestionRequest.getUsedAnswers();
        List<UsedAnswer> answers = new ArrayList<>();
        if (!answerIds.isEmpty()) {
            UsedQuestion finalUsedQuestion = usedQuestion;
            answerIds.forEach(answerId -> {
                UsedAnswer usedAnswer = usedAnswerService.getUsedAnswerById(answerId).get();
                usedAnswer.setUsedQuestion(finalUsedQuestion);
                usedAnswer = usedAnswerService.saveUsedAnswer(usedAnswer);
                answers.add(usedAnswer);
            });
        }
        usedQuestion.setUsedAnswers(new HashSet<>(answers));
        return new ResponseEntity<>(usedQuestionService.saveUsedQuestion(usedQuestion), HttpStatus.OK);
    }


    @DeleteMapping("/disable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<UsedQuestion> disableUsedQuestion(@RequestParam("used_question_id") Long usedQuestionId) {
        return usedQuestionService.updateUsedQuestion(usedQuestionId, false);
    }

    @GetMapping("/enable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<UsedQuestion> enableUsedQuestion(@RequestParam("used_question_id") Long usedQuestionId) {
        return usedQuestionService.updateUsedQuestion(usedQuestionId, true);
    }
}
