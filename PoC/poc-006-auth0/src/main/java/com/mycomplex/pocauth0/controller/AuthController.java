package com.mycomplex.pocauth0.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final String TENANT_CLAIM =
            "https://mycomplex.com/tenant_id";

    private static final String ROLES_CLAIM =
            "https://mycomplex.com/roles";

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "poc-006-auth0"
        );
    }

    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of(
                "message", "Endpoint publico funcionando"
        );
    }

    @GetMapping("/private")
    public Map<String, Object> privateEndpoint(
            @AuthenticationPrincipal Jwt jwt) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Usuario autenticado correctamente");
        response.put("subject", jwt.getSubject());
        response.put("issuer", jwt.getIssuer());
        response.put("audience", jwt.getAudience());
        response.put("tenantId", jwt.getClaim(TENANT_CLAIM));
        response.put("roles", jwt.getClaim(ROLES_CLAIM));

        return response;
    }
}
