package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {


    @Autowired
    private final StudentService studentService;

    @GetMapping(path = "/byId")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Student> getTeacherById(@RequestParam(name = "student_id") Long studentId){
        return new ResponseEntity<Student>(studentService.findStudentByAccountId(studentId), HttpStatus.OK);
    }



}
