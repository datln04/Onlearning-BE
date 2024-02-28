package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.repository.StudentRepository;
import fpt.fall23.onlearn.service.StudentService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

	@Override
	public Student findStudentByAccountId(Long accountId) {
		Optional<Student> student = studentRepository.findStudentByAccountId(accountId);
		if(!student.isEmpty() || student.isPresent()) {
			return student.get();
		}
		return null;
	}
    
    
    
    
    
}
