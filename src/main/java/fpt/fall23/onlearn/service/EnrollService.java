package fpt.fall23.onlearn.service;


import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.enums.EnrollStatus;


import java.util.List;

public interface EnrollService{

    Enroll getEnrollById(Long id);

    Enroll saveEnroll(Enroll enroll);

    List<Enroll> findEnrollByStudent(Long studentId);

    Enroll findEnrollByStudentAndCourse(Long studentId, Long courseId);


    List<Enroll> findEnrollByCourse(Long courseId);

    Enroll findEnrollByStudentAndCourse(Long studentId, Long courseId, EnrollStatus enrollStatus);

    List<Enroll> getAllEnrolls();


    Boolean removeEnroll(Long id);

    List<Enroll> findEnrollsByCourseAndProcessing(Long courseId);

    Boolean checkIsBannedAction(Long accountId);

}
