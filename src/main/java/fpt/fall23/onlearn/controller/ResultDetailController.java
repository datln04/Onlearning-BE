package fpt.fall23.onlearn.controller;

import java.util.List;

import fpt.fall23.onlearn.dto.resultdetail.ResultDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.resultdetail.ResultDetailView;
import fpt.fall23.onlearn.entity.ResultDetail;
import fpt.fall23.onlearn.service.ResultDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/result-detail")
public class ResultDetailController {

    @Autowired
    ResultDetailService resultDetailService;

//	ResultDetail saveResultDetail(ResultDetail resultDetail);
//
//	List<ResultDetail> findResultDetailByResultQuizId(Long resultQuizId);
//
//	ResultDetail findResultDetailById(Long id);

    @GetMapping(path = "/by-result-quiz")
    @JsonView(ResultDetailView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<ResultDetailResponse>> findAllByResultQuiz(
            @RequestParam(name = "result_quiz_id") Long resultQuizId) {
        return new ResponseEntity<>(resultDetailService.findResultDetailResponseByResultQuizId(resultQuizId),
                HttpStatus.OK);
    }


//	@GetMapping(path = "/getByIds")
//	@JsonView(ResultDetailView.class)
//	public ResponseEntity<List<ResultDetail>> findByIds(@RequestParam List<Long> ids){
//		return new ResponseEntity<List<ResultDetail>>(resultDetailService.findResultDetailByIds(ids), HttpStatus.OK);
//	}

}
