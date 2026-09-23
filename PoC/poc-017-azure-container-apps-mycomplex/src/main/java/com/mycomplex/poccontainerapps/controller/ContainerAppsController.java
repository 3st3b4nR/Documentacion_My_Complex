package com.mycomplex.poccontainerapps.controller;

import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContainerAppsController {

    @Value("${app.environment:local}")
    private String environment;

    @GetMapping("/")
    public Map<String, Object> root() throws Exception {
        return info();
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-017-container-apps"
        );
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("application", "MyComplex");
        response.put("poc", "ADR-017 Azure Container Apps");
        response.put("environment", environment);
        response.put("hostname", InetAddress.getLocalHost().getHostName());
        response.put("javaVersion", System.getProperty("java.version"));
        response.put("timestamp", Instant.now().toString());

        return response;
    }

    @GetMapping("/api/failure")
    public void failure() {
        throw new IllegalStateException(
            "Error intencional para validar recuperación de la plataforma"
        );
    }
}
