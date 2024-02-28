package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.RejectCourse;

import java.util.List;

public interface RejectCourseService {

    List<RejectCourse> findRejectCourseByCourse(Long courseId);

    RejectCourse saveRejectCourse(RejectCourse rejectCourse);

}
