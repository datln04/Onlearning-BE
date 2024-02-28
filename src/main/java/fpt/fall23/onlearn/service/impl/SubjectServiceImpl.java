package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.subject.UpdateSubjectStatus;
import fpt.fall23.onlearn.entity.Subject;
import fpt.fall23.onlearn.repository.*;
import fpt.fall23.onlearn.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {


    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    StaffRepository staffRepository;

    @Override
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAllSubject();
    }

    @Override
    public List<Subject> findAllSubjectsByName(String name) {
        return subjectRepository.findAllSubjectByName(name);
    }

    @Override
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findSubjectById(id);
    }

    @Override
    public Optional<Subject> saveSubject(Subject subject) {
        Subject result = subjectRepository.save(subject);
        return subjectRepository.findSubjectById(result.getId());
    }


    @Override
    public ResponseDTO<Subject> updateSubjectStatus(UpdateSubjectStatus updateSubjectStatus) {
        Optional<Subject> subjectOpt = subjectRepository.findSubjectById(updateSubjectStatus.getSubjectId());
        if (subjectOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Not found subject", null);
        }
        Subject subject = subjectOpt.get();
        subject.setStatus(updateSubjectStatus.getStatus());
        subject = subjectRepository.save(subject);
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", subject);
    }

    @Override
    public ResponseDTO<String> deleteSubject(Long subjectId) {
        return null;
    }
}
