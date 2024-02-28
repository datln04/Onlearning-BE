package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.RejectCourse;
import fpt.fall23.onlearn.repository.RejectCourseRepository;
import fpt.fall23.onlearn.service.RejectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RejectCourseServiceImpl implements RejectCourseService {
    @Autowired
    RejectCourseRepository rejectCourseRepository;

    @Override
    public List<RejectCourse> findRejectCourseByCourse(Long courseId) {
        return rejectCourseRepository.findAllByCourseId(courseId);
    }

    @Override
    public RejectCourse saveRejectCourse(RejectCourse rejectCourse) {
        return rejectCourseRepository.save(rejectCourse);
    }
}
