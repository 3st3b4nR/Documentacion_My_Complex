package com.mycomplex.pocdocker.controller;

import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class DockerController {

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
            "service", "poc-016-docker"
        );
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("application", "MyComplex");
        response.put("poc", "ADR-016 Docker");
        response.put("environment", environment);
        response.put("hostname", InetAddress.getLocalHost().getHostName());
        response.put("javaVersion", System.getProperty("java.version"));
        response.put("timestamp", Instant.now().toString());

        return response;
    }
}
