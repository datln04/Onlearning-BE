package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.entity.Syllabus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.repository.EnrollRepository;
import fpt.fall23.onlearn.repository.SyllabusRepository;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.StudentService;
import fpt.fall23.onlearn.service.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusRepository syllabusRepository;

    @Override
    public Syllabus getSyllabusById(Long id) {
        return syllabusRepository.getSyllabusById(id);
    }

    @Override
    public List<Syllabus> findAllSyllabus() {
        return syllabusRepository.findAll();
    }

    @Override
    public List<Syllabus> findSyllabusByCourseId(Long courseId) {
        return syllabusRepository.getSyllabusByCourseId(courseId);
    }

    @Override
    public List<Syllabus> findSyllabusByLessonId(Long lessonId) {
        return syllabusRepository.findSyllabusesByLessonId(lessonId);
    }

    @Override
    public Syllabus saveSyllabus(Syllabus syllabus) {
        return syllabusRepository.save(syllabus);
    }

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    StudentService studentService;

    @Autowired
    EnrollRepository enrollRepository;

    @Override
    public Syllabus getSyllabusByCourseEnrolled(Long course) {
        Optional<Account> accountOpt = authenticationService.getCurrentAuthenticatedAccount();
        if (accountOpt.isEmpty()) {
            return null;
        }
        Student student = studentService.findStudentByAccountId(accountOpt.get().getId());
        Enroll enroll = enrollRepository.findEnrollByStudentIdAndCourseIdAndStatusIn(student.getId(), course);
        if(enroll == null){
            return null;
        }
        Syllabus syllabus = enrollRepository.findSyllabusByEnroll(enroll.getId());
        return syllabus;
    }
}
