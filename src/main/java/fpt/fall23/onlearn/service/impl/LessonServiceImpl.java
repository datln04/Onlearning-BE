package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Lesson;
import fpt.fall23.onlearn.repository.LessonRepository;
import fpt.fall23.onlearn.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    LessonRepository lessonRepository;

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).get();
    }

    @Override
    public List<Lesson> findAllLesson() {
        return lessonRepository.findAll();
    }

    @Override
    public List<Lesson> findLessonsByCourseId(Long courseId) {
        return lessonRepository.findLessonByCourseId(courseId);
    }
    
    @Override
    public List<Lesson> getLessonsByIds(List<Long> lessonIds) {
        return lessonRepository.findAllById(lessonIds);
    }


    @Override
    public List<Lesson> findLessonBySyllabusId(Long syllabusId) {
        return lessonRepository.findLessonsBySyllabusId(syllabusId);
    }
}
