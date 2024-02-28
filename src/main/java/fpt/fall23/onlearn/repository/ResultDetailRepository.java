package fpt.fall23.onlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fpt.fall23.onlearn.entity.ResultDetail;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, Long>{

	List<ResultDetail> findResultDetailsByResultQuizId(Long id);
	List<ResultDetail> findResultDetailByIdIn(List<Long> ids);

//	List<ResultDetail> findAllByStudentId(Long studentId);
	
}
