package com.mycomplex.pocauthorization.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-007-authorization"
        );
    }

    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of(
            "message", "Endpoint publico"
        );
    }

    @GetMapping("/api/me")
    public Map<String, Object> me(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("subject", jwt.getSubject());
        response.put(
            "tenantId",
            jwt.getClaim("https://mycomplex.com/tenant_id")
        );
        response.put(
            "roles",
            jwt.getClaim("https://mycomplex.com/roles")
        );
        response.put("authorities", authentication.getAuthorities());

        return response;
    }

    @GetMapping("/api/admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Map<String, Object> adminOnly() {
        return Map.of(
            "message",
            "Acceso autorizado para Administrador"
        );
    }

    @GetMapping("/api/residente")
    @PreAuthorize("hasAnyRole('RESIDENTE', 'ADMINISTRADOR')")
    public Map<String, Object> residentArea() {
        return Map.of(
            "message",
            "Acceso autorizado para Residente o Administrador"
        );
    }

    @GetMapping("/api/tenants/{tenantId}/datos")
    @PreAuthorize("@tenantAuthorization.canAccess(#tenantId, #jwt)")
    public Map<String, Object> tenantData(
            @PathVariable String tenantId,
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
            "message", "Acceso al tenant autorizado",
            "tenantId", tenantId,
            "subject", jwt.getSubject()
        );
    }

    @GetMapping("/api/tenants/{tenantId}/admin")
    @PreAuthorize(
        "hasRole('ADMINISTRADOR') and " +
        "@tenantAuthorization.canAccess(#tenantId, #jwt)"
    )
    public Map<String, Object> tenantAdmin(
            @PathVariable String tenantId,
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
            "message",
            "Administrador autorizado dentro de su propio tenant",
            "tenantId",
            tenantId,
            "subject",
            jwt.getSubject()
        );
    }
}
