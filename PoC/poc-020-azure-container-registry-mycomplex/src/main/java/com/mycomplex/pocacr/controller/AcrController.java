package com.mycomplex.pocacr.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class AcrController {

    @Value("${app.version:dev}")
    private String version;

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-020-acr"
        );
    }

    @GetMapping("/api/version")
    public Map<String, Object> version() {
        return Map.of(
            "application", "MyComplex",
            "poc", "ADR-020 Azure Container Registry",
            "version", version,
            "timestamp", Instant.now().toString()
        );
    }
}
