package fpt.fall23.onlearn.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.entity.Answer;
import fpt.fall23.onlearn.service.AnswerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {
	

	
		@Autowired
		AnswerService answerService;

		@GetMapping(path = "/by-question")
		@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
		public ResponseEntity<List<Answer>> findAllByQuestion(@RequestParam(name = "question_id") Long questionId){
			return new ResponseEntity<List<Answer>>(answerService.findAllAnswerByQuestionId(questionId), HttpStatus.OK);
		}
		
		@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
		@GetMapping(path = "/answers")
		public ResponseEntity<List<Answer>> findAllAnswers(){
			return new ResponseEntity<List<Answer>>(answerService.listAllAnswers(), HttpStatus.OK);
		}
		
		
		@PostMapping(path = "/save")
		@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
		public ResponseEntity<Answer> saveAnswer(@RequestBody Answer answer){
			return new ResponseEntity<Answer>(answerService.saveAnswer(answer), HttpStatus.OK);
		}
		
		@GetMapping(path = "/by-id")
		@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
		public ResponseEntity<Answer> getById(@RequestParam(name = "id")Long id){
			return new ResponseEntity<Answer>(answerService.getAnswerById(id), HttpStatus.OK);
		}
		
		
		
		
		
	
}
