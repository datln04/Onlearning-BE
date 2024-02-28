package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	
	Optional<Student> findStudentByAccountId(Long accountId);

	Student findStudentById(Long studentId);
}
