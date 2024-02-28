package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.rejectcourse.RejectCourseView;
import fpt.fall23.onlearn.entity.Course;
import fpt.fall23.onlearn.entity.RejectCourse;
import fpt.fall23.onlearn.service.RejectCourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reject-course")
public class RejectCourseController {

    @Autowired
    RejectCourseService rejectCourseService;


    @GetMapping("/reject-courses")
    @JsonView(RejectCourseView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<RejectCourse>> approveCourse(@RequestParam(name = "course_id") Long courseId) {
        return new ResponseEntity<>(rejectCourseService.findRejectCourseByCourse(courseId), HttpStatus.OK);
    }

}
