package fpt.fall23.onlearn.controller;


import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.lesson.LessonRequest;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.entity.Course;
import fpt.fall23.onlearn.entity.Lesson;
import fpt.fall23.onlearn.entity.Syllabus;
import fpt.fall23.onlearn.enums.LessonType;
import fpt.fall23.onlearn.service.CourseService;
import fpt.fall23.onlearn.service.LessonService;
import fpt.fall23.onlearn.service.ResourceService;
import fpt.fall23.onlearn.service.SyllabusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lesson")
public class LessonController {

    @Autowired
    LessonService lessonService;

    @GetMapping("/byId")
    @JsonView(LessonView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Lesson> getLessonById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(lessonService.getLessonById(id), HttpStatus.OK);
    }

    @GetMapping("/lessons")
    @JsonView(LessonView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Lesson>> getAllLesson() {
        return new ResponseEntity<>(lessonService.findAllLesson(), HttpStatus.OK);
    }


    @Autowired
    CourseService courseService;


    @Autowired
    ResourceService resourceService;

    @Autowired
    SyllabusService syllabusService;

    @PostMapping("/save")
    @JsonView(LessonView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Lesson> saveLesson(@RequestBody LessonRequest lessonRequest) {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonRequest, lesson);
        lesson.setType(lessonRequest.getType().equalsIgnoreCase("VIDEO") ? LessonType.VIDEO : LessonType.READING);
        Course course = courseService.getCourseById(Long.valueOf(lessonRequest.getCourseId()));
        if (course != null) {
            lesson.setCourse(course);
        }
        if (lesson.getId() != 0 && lesson.getId() != null) {
            List<Syllabus> syllabus = syllabusService.findSyllabusByLessonId(lesson.getId());
            lesson.setSyllabuses(syllabus.stream().collect(Collectors.toSet()));
        }

        List<Syllabus> syllabusList = new ArrayList<>();
        if (lessonRequest.getSyllabusIds() != null || lessonRequest.getSyllabusIds().size() > 0) {
            for (String syllabusId :
                    lessonRequest.getSyllabusIds()) {
                Syllabus syllabus = syllabusService.getSyllabusById(Long.valueOf(syllabusId));
                syllabusList.add(syllabus);
            }
        }
        lesson.setDateTime(LocalDateTime.now());
        lesson.setSyllabuses(syllabusList.stream().collect(Collectors.toSet()));
        return new ResponseEntity<>(lessonService.saveLesson(lesson), HttpStatus.OK);
    }


    @GetMapping("/byCourseId")
    @JsonView(LessonView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Lesson>> findLessonByCourseId(@RequestParam(name = "course_id") Long courseid) {
        return new ResponseEntity<>(lessonService.findLessonsByCourseId(courseid), HttpStatus.OK);
    }

    @GetMapping("/by-syllabus")
    @JsonView(LessonView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Lesson>> findLessonBySyllabusId(@RequestParam(name = "syllabus_id") Long syllabusId) {
        return new ResponseEntity<>(lessonService.findLessonBySyllabusId(syllabusId), HttpStatus.OK);
    }


}
