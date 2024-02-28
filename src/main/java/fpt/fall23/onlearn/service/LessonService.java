package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Lesson;

import java.util.List;

public interface LessonService {

    Lesson saveLesson(Lesson lesson);
    Lesson getLessonById(Long id);

    List<Lesson> findAllLesson();

    List<Lesson> findLessonsByCourseId(Long courseId);
    
    public List<Lesson> getLessonsByIds(List<Long> lessonIds);


    List<Lesson> findLessonBySyllabusId(Long syllabusId);

}
