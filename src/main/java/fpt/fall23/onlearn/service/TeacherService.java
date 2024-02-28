package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Teacher;


public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    Teacher getTeacherByAccountId(Long accountId);
    
}
