package com.mycomplex.pocpush.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    public String sendToToken(String token, String title, String body)
            throws FirebaseMessagingException {

        Message message = Message.builder()
            .setToken(token)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .putData("source", "mycomplex-poc-009")
            .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
