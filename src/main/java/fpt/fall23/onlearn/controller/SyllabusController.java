package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.syllabus.SyllabusRequest;
import fpt.fall23.onlearn.dto.syllabus.SyllabusView;
import fpt.fall23.onlearn.entity.Lesson;
import fpt.fall23.onlearn.entity.Syllabus;
import fpt.fall23.onlearn.service.CourseService;
import fpt.fall23.onlearn.service.LessonService;
import fpt.fall23.onlearn.service.SyllabusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/syllabus")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;

    @GetMapping("/byId")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Syllabus> getSyllabusById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(syllabusService.getSyllabusById(id), HttpStatus.OK);
    }

    @GetMapping("/syllabus")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Syllabus>> getAllSyllabus() {
        return new ResponseEntity<>(syllabusService.findAllSyllabus(), HttpStatus.OK);
    }

    @GetMapping("/byLessonId")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Syllabus>> findSyllabusByLessonId(@RequestParam(name = "lesson_id") Long lessonId) {
        return new ResponseEntity<>(syllabusService.findSyllabusByLessonId(lessonId), HttpStatus.OK);
    }

    @Autowired
    CourseService courseService;

    @Autowired
    LessonService lessonService;

    @PostMapping("/save")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Syllabus> saveSyllabus(@RequestBody SyllabusRequest syllabusRequest) {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(syllabusRequest.getId());
        syllabus.setName(syllabusRequest.getName());
        syllabus.setCourse(courseService.getCourseById(syllabusRequest.getCourseId()));
        syllabus.setStatus(syllabusRequest.getStatus());
        syllabus.setCreateDate(LocalDateTime.now());
        List<Long> lessonIds = syllabusRequest.getLessonIds();
        if (lessonIds != null && !lessonIds.isEmpty()) {
            Set<Lesson> lessons = lessonService.getLessonsByIds(lessonIds).stream().collect(Collectors.toSet()); // Retrieve
            // lessons
            // by
            // their
            // IDs
            syllabus.setLessons(lessons); // Associate the lessons with the syllabus
        }
        syllabus = syllabusService.saveSyllabus(syllabus);
        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @GetMapping("/byCourseId")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Syllabus>> findSyllabusByCourseId(@RequestParam(name = "course_id") Long courseid) {
        return new ResponseEntity<>(syllabusService.findSyllabusByCourseId(courseid), HttpStatus.OK);
    }

    @GetMapping("/by-course-enroll")
    @JsonView(SyllabusView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Syllabus> findSyllabusByCourseEnrolled(@RequestParam(name = "course_id") Long courseid) {
        Syllabus syllabus = syllabusService.getSyllabusByCourseEnrolled(courseid);
        if (syllabus == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

}
