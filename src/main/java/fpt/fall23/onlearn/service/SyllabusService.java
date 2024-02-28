package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Syllabus;

import java.util.List;

public interface SyllabusService {
    
	Syllabus getSyllabusById(Long id);
    
    List<Syllabus> findAllSyllabus();

    List<Syllabus> findSyllabusByCourseId(Long courseId);

    Syllabus getSyllabusByCourseEnrolled(Long course);

    
    List<Syllabus> findSyllabusByLessonId(Long lessonId);
    
    
    Syllabus saveSyllabus(Syllabus syllabus);

}
