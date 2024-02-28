package fpt.fall23.onlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.service.TeacherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
	
	
	@Autowired
	TeacherService teacherService;
	
	@GetMapping(path = "/byId")
	@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
	public ResponseEntity<Teacher> getTeacherById(@RequestParam(name = "teacher_id") Long teacherId){
		return new ResponseEntity<Teacher>(teacherService.getTeacherById(teacherId), HttpStatus.OK);
	}
	
	
	
}
