package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Student;


import org.springframework.stereotype.Service;

@Service
public interface StudentService {
    Student saveStudent(Student student);

    Student findStudentById(Long id);
    
    Student findStudentByAccountId(Long accountId);
    

}
