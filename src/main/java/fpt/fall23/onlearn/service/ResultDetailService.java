package fpt.fall23.onlearn.service;

import java.util.List;

import fpt.fall23.onlearn.dto.resultdetail.ResultDetailResponse;
import fpt.fall23.onlearn.entity.ResultDetail;

public interface ResultDetailService {

	ResultDetail saveResultDetail(ResultDetail resultDetail);

	List<ResultDetailResponse> findResultDetailResponseByResultQuizId(Long resultQuizId);


	List<ResultDetail> findResultDetailByResultQuizId(Long resultQuizId);
	List<ResultDetail> findResultDetailByStudentId(Long studentId);

	ResultDetail findResultDetailById(Long id);
	List<ResultDetail> findResultDetailByIds(List<Long> ids);

}
