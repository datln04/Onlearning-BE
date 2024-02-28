package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.repository.TeacherRepository;
import fpt.fall23.onlearn.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
	@Autowired
	TeacherRepository teacherRepository;

	@Override
	public Teacher saveTeacher(Teacher teacher) {
		return teacherRepository.save(teacher);
	}

	@Override
	public Teacher getTeacherById(Long id) {
		Optional<Teacher> teacher = teacherRepository.findById(id);
		if (teacher.isPresent()) {
			return teacher.get();
		}
		return null;
	}

	@Override
	public Teacher getTeacherByAccountId(Long accountId) {
		Optional<Teacher> teacher = teacherRepository.findTeacherByAccountId(accountId);
		if (!teacher.isEmpty() || teacher.isPresent()) {
			return teacher.get();
		}
		return null;
	}
}
