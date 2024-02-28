package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail-sender")
public class MailSenderController {

    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<String> sendEmail(@RequestParam(name = "receive") String receive, @RequestParam(name = "subject")
    String subject, @RequestParam(name = "content") String content) {
        emailService.sendEmail("cunplong.1@gmail.com", receive, subject, content);
        return new ResponseEntity<>("Done"
                , HttpStatus.OK);
    }

}
