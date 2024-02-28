package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.repository.AccountRepository;
import fpt.fall23.onlearn.repository.EnrollRepository;
import fpt.fall23.onlearn.repository.StudentRepository;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollServiceImpl implements EnrollService {

    @Autowired
    EnrollRepository enrollRepository;

    @Override
    public Enroll getEnrollById(Long id) {
        Optional<Enroll> enroll = enrollRepository.findById(id);
        if (enroll.isPresent()) {
            return enroll.get();
        }
        return null;
    }

    @Override
    public Enroll saveEnroll(Enroll enroll) {
        return enrollRepository.save(enroll);
    }

    @Override
    public List<Enroll> findEnrollByStudent(Long studentId) {
        return enrollRepository.findEnrollByStudentId(studentId);
    }

    @Override
    public List<Enroll> findEnrollByCourse(Long courseId) {
        return enrollRepository.findEnrollByCourseId(courseId);
    }

    @Override
    public List<Enroll> getAllEnrolls() {
        return enrollRepository.findAll();
    }

    @Override
    public Enroll findEnrollByStudentAndCourse(Long studentId, Long courseId, EnrollStatus enrollStatus) {
        return enrollRepository.findEnrollByStudentIdAndCourseIdAndStatus(studentId, courseId, enrollStatus);
    }

    @Override
    public Boolean removeEnroll(Long id) {
        Enroll enroll = enrollRepository.getEnrollById(id);
        if (enroll != null) {
            enroll.setStatus(EnrollStatus.REMOVED);

            enrollRepository.save(enroll);
            return true;
        }
        return false;
    }

    @Override
    public List<Enroll> findEnrollsByCourseAndProcessing(Long courseId) {
        return enrollRepository.findEnrollsByCourseIdAndStatus(courseId, EnrollStatus.PROCESSING);
    }

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Override
    public Boolean checkIsBannedAction(Long accountId) {
        Account account = authenticationService.findAccountById(accountId);
        if (account == null) {
            return false;
        }
        if (account.getRole().equals(Role.TEACHER)) {
            return false;
        }

        Optional<Student> studentOpt = studentRepository.findStudentByAccountId(account.getId());
        if (studentOpt.isEmpty()) {
            return false;
        }
        Student student = studentOpt.get();
        List<Enroll> enrolls = findEnrollByStudent(student.getId());
        for (Enroll e :
                enrolls) {
            if (e.isBanned()) {
                return true;
            }

        }
        return false;
    }

    @Override
    public Enroll findEnrollByStudentAndCourse(Long studentId, Long courseId) {
//        List<EnrollStatus> enrollStatuses = new ArrayList<>();
//        enrollStatuses.add(EnrollStatus.PROCESSING);
//        enrollStatuses.add(EnrollStatus.DONE);
        return enrollRepository.findEnrollByStudentIdAndCourseIdAndStatusIn(studentId, courseId);
    }
}