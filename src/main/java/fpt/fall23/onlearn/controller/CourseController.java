package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.course.CourseRequest;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.course.RejectCourseRequest;
import fpt.fall23.onlearn.dto.course.request.CourseFilter;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.CourseStatus;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.CurrencyConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping("/courses")
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> getAllCourses() {
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/by-status-active")
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> getAllByStatus() {
        return new ResponseEntity<>(courseService.findAllByStatus(CourseStatus.ACTIVE), HttpStatus.OK);
    }

    @GetMapping("/search")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> searchCourse(@RequestParam(name = "value", defaultValue = "") String value,
                                                     @RequestParam(name = "minPrice", defaultValue = "") Double minPrice,
                                                     @RequestParam(name = "maxPrice", defaultValue = "") Double maxPrice) {
        CourseFilter courseFilter = new CourseFilter(value, minPrice, maxPrice);
        return new ResponseEntity<>(courseService.getCourseByFilter(courseFilter), HttpStatus.OK);
    }

    @PostMapping("/search")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> filterCourse(@RequestBody CourseFilter courseFilter) {
        return new ResponseEntity<>(courseService.getCourseByFilter(courseFilter), HttpStatus.OK);
    }

    @Autowired
    LessonService lessonService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    WalletService walletService;

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/save")
    @JsonView(CourseView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Course> saveCourse(@RequestBody CourseRequest courseRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(courseRequest, course);
        Teacher teacher = teacherService.getTeacherById(courseRequest.getTeacherId());

        if (teacher != null) {
            course.setTeacher(teacher);
        }
        Optional<Subject> subject = subjectService.getSubjectById(courseRequest.getSubjectId());
        if (subject.isPresent()) {
            course.setSubject(subject.get());
        }

        if (course.getPrice() <= subject.get().getMinPrice()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


//        List<Lesson> lessons = new ArrayList<>();
//        if(!courseRequest.getLessonIds().isEmpty()){
//            courseRequest.getLessonIds().forEach(lessonId -> {
//                Lesson lesson = lessonService.getLessonById(Long.valueOf(lessonId));
//                lessons.add(lesson);
//            });
//        }
//        if(lessons.size() > 0){
//            course.setLessons(lessons.stream().collect(Collectors.toSet()));
//        }
        course.setCreateDate(LocalDateTime.now());
        return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.OK);
    }

    @GetMapping("/charge-fee-teacher")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Transaction> chargeFeeTeacher(@RequestParam(name = "teacher_id") Long teacherId,
                                                     @RequestParam(name = "course_id") Long courseId) {
        return courseService.chargeFeeForTeacher(teacherId, courseId);
    }

    @GetMapping("/byId")
    @JsonView(CourseView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Course> getCourseById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
    }

    @GetMapping("/bySubjectId")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> findAllCourseBySubjectId(@RequestParam(name = "subject-id") Long subjectId) {
        return new ResponseEntity<>(courseService.getCourseBySubject(subjectId), HttpStatus.OK);
    }

    @GetMapping("/byTeacherId")
    //@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> findAllCourseByTeacherId(@RequestParam(name = "teacher-id") Long teacherId) {
        return new ResponseEntity<>(courseService.getCourseByTeacherId(teacherId), HttpStatus.OK);
    }

    @GetMapping("/un-enrolled-by-student")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(CourseView.class)
    public ResponseEntity<List<Course>> findAllCourseUnEnrolledByStudent(@RequestParam(name = "student_id") Long studentId,
                                                                         @RequestParam(name = "status") String status,
                                                                         @RequestParam(name = "value", defaultValue = "") String value) {
        return new ResponseEntity<>(courseService.getCourseUnEnrolled(studentId, status, value), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<String> deleteCourse(@RequestParam(name = "id") Long id) {
        if (courseService.getCourseById(id) != null) {
            courseService.deleteCourse(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("fail", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/approve")
    @JsonView(CourseView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Course>> approveCourse(@RequestParam(name = "course_id") Long courseId) {
        return new ResponseEntity<>(courseService.approveCourse(courseId), HttpStatus.OK);
    }

    @PostMapping("/reject")
    @JsonView(CourseView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Course>> rejectCourse(@RequestBody RejectCourseRequest rejectCourseRequest) {
        return new ResponseEntity<>(courseService.rejectCourse(rejectCourseRequest), HttpStatus.OK);
    }

    @DeleteMapping("/remove-course")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Course> removeCourse(@RequestParam(name = "course_id") Long courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Not found course", null);
        }
        course = courseService.removeCourse(courseId);
        if (course == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Can't remove course", null);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "Remove success", course);
    }

    @GetMapping("/count-enrolled")
    //@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Integer> getCountEnrolledByCourse(@RequestParam(name = "course_id") Long courseId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", courseService.countTotalEnrolledByCourse(courseId));
    }

}
