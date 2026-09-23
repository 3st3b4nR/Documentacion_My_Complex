package com.mycomplex.pocpush.controller;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.mycomplex.pocpush.dto.PushRequest;
import com.mycomplex.pocpush.dto.PushResponse;
import com.mycomplex.pocpush.service.PushNotificationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PushController {

    private final PushNotificationService pushNotificationService;

    public PushController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-009-firebase-push"
        );
    }

    @PostMapping("/api/notifications/send")
    public ResponseEntity<PushResponse> send(
            @Valid @RequestBody PushRequest request) {

        try {
            String messageId = pushNotificationService.sendToToken(
                request.token(),
                request.title(),
                request.body()
            );

            return ResponseEntity.ok(
                new PushResponse(
                    true,
                    messageId,
                    "Notificacion enviada a Firebase Cloud Messaging"
                )
            );

        } catch (FirebaseMessagingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(
                    new PushResponse(
                        false,
                        null,
                        "Firebase rechazo el envio: " + ex.getMessage()
                    )
                );
        }
    }
}
