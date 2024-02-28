package fpt.fall23.onlearn.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

public class FirebaseMessageSnippets {


    public void sendToken() throws FirebaseMessagingException {
        //come from the client FCM SDKs
        String registrationToken = "";

        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        //Response is a message Id
        System.out.println("Successfully sent message: " + response);

    }
}