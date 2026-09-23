package com.mycomplex.pocemail.controller;

import java.util.Map;

import com.mycomplex.pocemail.dto.EmailRequest;
import com.mycomplex.pocemail.dto.EmailResponse;
import com.mycomplex.pocemail.service.ResendEmailService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    private final ResendEmailService emailService;

    public EmailController(ResendEmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-010-resend-email"
        );
    }

    @PostMapping("/api/emails/send")
    public ResponseEntity<EmailResponse> send(
            @Valid @RequestBody EmailRequest request) {

        try {
            EmailResponse result = emailService.send(
                request.to(),
                request.subject(),
                request.message()
            );

            if (result.success()) {
                return ResponseEntity.ok(result);
            }

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(result);

        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new EmailResponse(
                        false,
                        null,
                        500,
                        ex.getMessage()
                    )
                );

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(
                    new EmailResponse(
                        false,
                        null,
                        502,
                        "Error comunicandose con Resend: " + ex.getMessage()
                    )
                );
        }
    }
}
