package com.mycomplex.pocwompi;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/wompi")
public class WebhookController {
    private final WebhookSignatureVerifier verifier;
    private final WebhookService service;
    private final String eventSecret;

    public WebhookController(WebhookSignatureVerifier verifier, WebhookService service,
            @Value("${wompi.event-secret}") String eventSecret) {
        this.verifier = verifier;
        this.service = service;
        this.eventSecret = eventSecret;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> receive(
            @RequestHeader("X-Event-Checksum") String checksum,
            @RequestBody JsonNode event) {
        if (!verifier.isValid(event, checksum, eventSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("accepted", false));
        }
        boolean processed = service.process(event);
        return ResponseEntity.ok(Map.of("accepted", true, "processed", processed));
    }
}

