package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.report.ReportRequest;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Report;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.enums.ReportType;
import fpt.fall23.onlearn.enums.ResponseCode;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.exception.ApiException;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.ReportService;
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
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/save")
    @JsonView({ReportView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Report> saveReport(@RequestBody ReportRequest reportRequest) {
        Report report = new Report();
        BeanUtils.copyProperties(reportRequest, report);
        Teacher teacher = teacherService.getTeacherById(reportRequest.getTeacherId());
        Student student = studentService.findStudentById(reportRequest.getStudentId());
        report.setTeacher(teacher);
        report.setStudent(student);

        Account acc = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));
        if (acc.getRole().equals(Role.STUDENT)) {
            report.setReportType(ReportType.STUDENT);
        } else {
            report.setReportType(ReportType.TEACHER);
        }
        Report r = reportService.save(report);
        return new ResponseEntity<>(r, HttpStatus.OK);

    }

    @GetMapping("/by-teacher")
    @JsonView({ReportView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<List<Report>> getReportByTeacher(@RequestParam(name = "teacher_id") Long teacherId) {
        return reportService.getReportByTeacher(teacherId);
    }

    @GetMapping("/by-student")
    @JsonView({ReportView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<List<Report>> getReportByStudent(@RequestParam(name = "student_id") Long studentId) {
        return reportService.getReportByStudent(studentId);
    }

    @GetMapping("/reports")
    @JsonView({ReportView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<List<Report>> getAllReport() {
        return reportService.getAllReports();
    }

}
