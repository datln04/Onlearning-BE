package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.question.QuestionRequest;
import fpt.fall23.onlearn.dto.question.QuestionView;
import fpt.fall23.onlearn.entity.Answer;
import fpt.fall23.onlearn.entity.Course;
import fpt.fall23.onlearn.entity.Lesson;
import fpt.fall23.onlearn.entity.Question;
import fpt.fall23.onlearn.service.CourseService;
import fpt.fall23.onlearn.service.LessonService;
import fpt.fall23.onlearn.service.QuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CourseService courseService;

    @Autowired
    LessonService lessonService;

    @GetMapping("/byId")
    @JsonView(QuestionView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Question> getQuestionById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(questionService.getQuestionById(id), HttpStatus.OK);
    }

    @GetMapping("/byLessonId")
    @JsonView(QuestionView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Question>> findQuestionByLessonId(@RequestParam(name = "lesson_id") Long lessonId) {
        return new ResponseEntity<>(questionService.findAllQuestionByLessonId(lessonId), HttpStatus.OK);
    }

    @PostMapping("/save")
    @JsonView(QuestionView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Question> saveQuestion(@RequestBody QuestionRequest questionRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionRequest, question);

        // Create a new set to store Answer entities
        Set<Answer> answers = new HashSet<>();

        // Loop through the Answer data in the request and create Answer entities
        if (questionRequest.getAnswers() != null) {
            for (Answer answerRequest : questionRequest.getAnswers()) {
                Answer answer = new Answer();
                BeanUtils.copyProperties(answerRequest, answer);

                // Set the parent Question for the Answer
                answer.setQuestion(question);

                answers.add(answer);
            }
        }

        question.setAnswers(answers);

        Course course = courseService.getCourseById(Long.valueOf(questionRequest.getCourseId()));
        Lesson lesson = lessonService.getLessonById(Long.valueOf(questionRequest.getLessonId()));

        if (course != null) {
            question.setCourse(course);
        }

        if (lesson != null) {
            question.setLesson(lesson);
        }
        // Save the Question entity and then return it
        Question savedQuestion = questionService.saveQuestion(question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.OK);
    }

    @GetMapping(path = "/by-teacher")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(QuestionView.class)
    public ResponseEntity<List<Question>> findAllQuestionByTeacher(@RequestParam(name = "teacher_id") Long teacherId) {
        return new ResponseEntity<List<Question>>(questionService.findAllQuestionByTeacherId(teacherId), HttpStatus.OK);
    }

    @GetMapping(path = "/questions")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(QuestionView.class)
    public ResponseEntity<List<Question>> findAllQuestions() {
        return new ResponseEntity<List<Question>>(questionService.findAllQuestions(), HttpStatus.OK);
    }

    @GetMapping(path = "/by-id")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(QuestionView.class)
    public ResponseEntity<Question> findQuestionById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(questionService.getQuestionById(id), HttpStatus.OK);
    }

}
