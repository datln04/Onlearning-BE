package fpt.fall23.onlearn.controller;

import com.amazonaws.Response;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.profile.ProfileRequest;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Profile;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.repository.StudentRepository;
import fpt.fall23.onlearn.service.ProfileService;
import fpt.fall23.onlearn.service.StudentService;
import fpt.fall23.onlearn.service.TeacherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    TeacherService teacherService;


    @GetMapping("/profile-teacher")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Teacher> getTeacherProfile(@RequestParam(name = "teacher_id") Long teacherId) {
        return new ResponseEntity<>(teacherService.getTeacherById(teacherId), HttpStatus.OK);
    }

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/profile-student")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Student> getStudentProfile(@RequestParam(name = "student_id") Long studentId) {
        return new ResponseEntity<>(studentRepository.findStudentById(studentId), HttpStatus.OK);
    }


    @PostMapping("/save")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Profile> saveProfile(@RequestBody ProfileRequest profileRequest) {
        Profile profile = profileService.getProfileByEmail(profileRequest.getEmail());
        if (profile == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Not found profile", null);
        } else {
            Long profileId = profile.getId();
            BeanUtils.copyProperties(profileRequest, profile);
            profile.setId(profileId);
            profile = profileService.saveProfile(profile);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "Success", profile);
    }

//    @GetMapping("/rating-teacher")
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
//    public ResponseEntity<Double> getRatingTeacher(@RequestParam(name = "teacher_id") Long teacherId){
//
//
//        return new ResponseEntity<>(null);
//    }


}
