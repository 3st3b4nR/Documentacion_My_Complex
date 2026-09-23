package com.mycomplex.pockeyvault.controller;

import java.util.Map;

import com.mycomplex.pockeyvault.dto.SecretMetadataResponse;
import com.mycomplex.pockeyvault.dto.SecretResponse;
import com.mycomplex.pockeyvault.dto.SecretWriteRequest;
import com.mycomplex.pockeyvault.service.KeyVaultService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KeyVaultController {

    private final KeyVaultService keyVaultService;

    public KeyVaultController(KeyVaultService keyVaultService) {
        this.keyVaultService = keyVaultService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-019-azure-key-vault"
        );
    }

    @PostMapping("/api/secrets")
    public ResponseEntity<SecretResponse> save(
            @Valid @RequestBody SecretWriteRequest request) {

        return ResponseEntity.ok(
            keyVaultService.save(
                request.name(),
                request.value()
            )
        );
    }

    @GetMapping("/api/secrets/{name}/metadata")
    public ResponseEntity<SecretMetadataResponse> metadata(
            @PathVariable String name) {

        return ResponseEntity.ok(
            keyVaultService.metadata(name)
        );
    }

    @PostMapping("/api/secrets/{name}/verify")
    public ResponseEntity<SecretResponse> verify(
            @PathVariable String name,
            @RequestBody Map<String, String> body) {

        String expectedValue = body.get("expectedValue");

        if (expectedValue == null) {
            throw new IllegalArgumentException(
                "Debe enviar expectedValue."
            );
        }

        return ResponseEntity.ok(
            keyVaultService.verify(name, expectedValue)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> badRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
            Map.of(
                "success", false,
                "error", ex.getMessage()
            )
        );
    }
}
