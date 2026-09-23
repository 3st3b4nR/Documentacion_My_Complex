package com.mycomplex.pocwaf.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WafTestController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "service", "poc-012-cloudflare-waf",
            "message", "Origen MyComplex disponible",
            "status", "UP"
        );
    }

    @GetMapping("/public")
    public Map<String, Object> publicEndpoint(HttpServletRequest request) {
        return requestInfo(
            "TRAFICO_PERMITIDO",
            "Si ves esta respuesta, Cloudflare permitió la solicitud.",
            request
        );
    }

    @GetMapping("/poc-waf-block")
    public Map<String, Object> blockTarget(HttpServletRequest request) {
        return requestInfo(
            "ORIGEN_ALCANZADO",
            "Esta respuesta solo debe verse ANTES de activar la regla WAF. " +
            "Después, Cloudflare debe bloquear la solicitud antes de que llegue aquí.",
            request
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-012-cloudflare-waf"
        );
    }

    private Map<String, Object> requestInfo(
            String result,
            String message,
            HttpServletRequest request) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", result);
        response.put("message", message);
        response.put("path", request.getRequestURI());
        response.put("method", request.getMethod());
        response.put("timestamp", Instant.now().toString());

        // Cabeceras útiles para comprobar que la petición pasó por Cloudflare.
        response.put("cfRay", request.getHeader("CF-Ray"));
        response.put("cfConnectingIp", request.getHeader("CF-Connecting-IP"));

        return response;
    }
}
