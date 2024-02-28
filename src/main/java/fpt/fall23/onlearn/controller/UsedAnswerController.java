package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.used.answer.UsedAnswerRequest;
import fpt.fall23.onlearn.entity.UsedAnswer;
import fpt.fall23.onlearn.service.UsedAnswerService;
import fpt.fall23.onlearn.service.UsedQuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/used-answer")
public class UsedAnswerController {

	@Autowired
	UsedAnswerService usedAnswerService;

	@Autowired
	UsedQuestionService usedQuestionService;

	@PostMapping("/save")
	@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
	public ResponseEntity<UsedAnswer> saveUsedAnswer(@RequestBody UsedAnswerRequest usedAnswerRequest) {
		UsedAnswer usedAnswer = new UsedAnswer();
		BeanUtils.copyProperties(usedAnswerRequest, usedAnswer);
//        usedQuestionService.getUsedQuestionById(usedAnswerRequest.getUsedQuestionId()).get();
		return new ResponseEntity<>(usedAnswerService.saveUsedAnswer(usedAnswer), HttpStatus.OK);
	}
	@PostMapping("/saveAll")
	@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
	public ResponseEntity<List<UsedAnswer>> saveUsedAnswer(@RequestBody List<UsedAnswerRequest> usedAnswerRequests) {
		List<UsedAnswer> list = new ArrayList<>();
		for (UsedAnswerRequest answerRequest: usedAnswerRequests
			 ) {
			UsedAnswer usedAnswer = new UsedAnswer();
			BeanUtils.copyProperties(answerRequest, usedAnswer);
			list.add(usedAnswer);
		}

//        usedQuestionService.getUsedQuestionById(usedAnswerRequest.getUsedQuestionId()).get();
		return new ResponseEntity<>(usedAnswerService.saveListUsedAnswer(list), HttpStatus.OK);
	}


	@GetMapping("/by-used-question")
	@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
	public ResponseEntity<List<UsedAnswer>> getByUsedQuestionId(
			@RequestParam(name = "used_question_id") Long usedQuestionId) {
		return new ResponseEntity<List<UsedAnswer>>(usedAnswerService.findAllByUsedQuestionId(usedQuestionId),
				HttpStatus.OK);
	}

}
