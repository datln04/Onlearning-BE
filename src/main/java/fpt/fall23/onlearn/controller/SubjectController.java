package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.subject.SubjectRequest;
import fpt.fall23.onlearn.dto.subject.SubjectView;
import fpt.fall23.onlearn.dto.subject.UpdateSubjectStatus;
import fpt.fall23.onlearn.entity.Staff;
import fpt.fall23.onlearn.entity.Subject;
import fpt.fall23.onlearn.service.StaffService;
import fpt.fall23.onlearn.service.SubjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subject")
@RequiredArgsConstructor
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping(path = "/subjects")
    @JsonView(SubjectView.class)
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Subject>> findAllSubject() {
        return new ResponseEntity<>(subjectService.findAllSubjects(), HttpStatus.OK);
    }

    @GetMapping(path = "/byName")
    @JsonView(SubjectView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Subject>> findAllSubjectByName(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>(subjectService.findAllSubjectsByName(name), HttpStatus.OK);
    }

    @GetMapping(path = "/byId")
    @JsonView(SubjectView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Optional<Subject>> findSubjectById(@RequestParam("subject-id") Long subjectId) {
        return new ResponseEntity<>(subjectService.getSubjectById(subjectId), HttpStatus.OK);
    }

    @Autowired
    StaffService staffService;

    @PostMapping(path = "/save")
    @JsonView(SubjectView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Optional<Subject>> saveSubject(@RequestBody SubjectRequest subjectRequest) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectRequest, subject);
        Staff staff = staffService.getStaffById(Long.valueOf(subjectRequest.getStaffId()));
        if (staff != null) {
            subject.setStaff(staff);
        }
        return new ResponseEntity<>(subjectService.saveSubject(subject), HttpStatus.OK);
    }

    @PostMapping("/update-status")
    @JsonView(SubjectView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Subject>> disableSubject(@RequestBody UpdateSubjectStatus updateSubjectStatus) {
        return new ResponseEntity<>(subjectService.updateSubjectStatus(updateSubjectStatus), HttpStatus.OK);
    }


//    @DeleteMapping("/delete")
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
//    public ResponseDTO<String> deleteSubject(@RequestParam("subject_id") Long id) {
//
//        return new ResponseEntity<>(subjectService.de`(updateSubjectStatus), HttpStatus.OK);
//    }

}
