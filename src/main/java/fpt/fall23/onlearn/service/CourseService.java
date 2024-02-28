package fpt.fall23.onlearn.service;


import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.course.CourseRequest;
import fpt.fall23.onlearn.dto.course.CourseResponse;
import fpt.fall23.onlearn.dto.course.RejectCourseRequest;
import fpt.fall23.onlearn.dto.course.request.CourseFilter;
import fpt.fall23.onlearn.entity.Course;
import fpt.fall23.onlearn.entity.Transaction;
import fpt.fall23.onlearn.entity.Wallet;
import fpt.fall23.onlearn.enums.CourseStatus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    Course saveCourse(Course course);

    Course teacherSaveCourse(CourseRequest courseRequest);

    ResponseDTO<Transaction> chargeFeeForTeacher(Long teacherId, Long courseId);


    List<Course> getAllCourses();

    List<Course> getCourseByFilter(CourseFilter courseFilter);


    Course getCourseById(Long id);

    void deleteCourse(Long id);

    Course removeCourse(Long id);


    List<Course> getCourseBySubject(Long subjectId);

    List<Course> getCourseByTeacherId(Long teacherId);

    List<Course> getCourseUnEnrolled(Long accountId, String status, String value);

    Page<CourseResponse> getCourseEnrolls(Long studentId, EnrollStatus status, Pageable pageable);


    ResponseDTO<Course> approveCourse(Long id);

    ResponseDTO<Course> rejectCourse(RejectCourseRequest rejectCourseRequest);


    Integer countTotalEnrolledByCourse(Long courseId);


    List<Course> findAllByStatus(CourseStatus courseStatus);
}
