package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.subject.UpdateSubjectStatus;
import fpt.fall23.onlearn.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    List<Subject> findAllSubjects();

    List<Subject> findAllSubjectsByName(String name);

    Optional<Subject> getSubjectById(Long id);

    Optional<Subject> saveSubject(Subject subject);


    ResponseDTO<Subject> updateSubjectStatus(UpdateSubjectStatus updateSubjectStatus);


    ResponseDTO<String> deleteSubject(Long subjectId);
}
