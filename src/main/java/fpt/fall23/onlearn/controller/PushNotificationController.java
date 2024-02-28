package fpt.fall23.onlearn.controller;
import java.util.List;
import java.util.Map;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.firebase.PnsRequest;
import fpt.fall23.onlearn.service.impl.FCMService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.BatchResponse;

@RestController
@RequestMapping("/api/v1/notification")
public class PushNotificationController {



    @Autowired
    private FCMService fcmService;

    @PostMapping("/send-notification")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<BatchResponse> sendSampleNotification(@RequestBody PnsRequest pnsRequest) {
//        List<String> token = pnsRequest.getToken();
//        String title = pnsRequest.getTitle();
//        String message = pnsRequest.getMessage();
        return new ResponseEntity<BatchResponse>(fcmService.pushNotification(pnsRequest), HttpStatus.OK);
    }

    @PostMapping("/receive-notification")
    public void receiveNotification(@RequestBody Map<String, Object> payload) {
        System.out.println("Received notification:");
        System.out.println(payload);
    }


}