package com.mycomplex.pocemail.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycomplex.pocemail.config.ResendProperties;
import com.mycomplex.pocemail.dto.EmailResponse;

import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ResendProperties properties;

    public ResendEmailService(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            ResendProperties properties) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public EmailResponse send(
            String to,
            String subject,
            String message) throws Exception {

        validateConfiguration();

        Map<String, Object> payload = Map.of(
            "from", properties.from(),
            "to", List.of(to),
            "subject", subject,
            "html", buildHtml(message)
        );

        String json = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(properties.apiUrl()))
            .timeout(Duration.ofSeconds(20))
            .header("Authorization", "Bearer " + properties.apiKey())
            .header("Content-Type", "application/json")
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    json,
                    StandardCharsets.UTF_8
                )
            )
            .build();

        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            JsonNode body = objectMapper.readTree(response.body());
            String id = body.path("id").asText(null);

            return new EmailResponse(
                true,
                id,
                status,
                "Resend acepto el correo para envio"
            );
        }

        return new EmailResponse(
            false,
            null,
            status,
            "Resend rechazo la solicitud: " + response.body()
        );
    }

    private void validateConfiguration() {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) {
            throw new IllegalStateException(
                "Falta configurar RESEND_API_KEY."
            );
        }

        if (properties.from() == null || properties.from().isBlank()) {
            throw new IllegalStateException(
                "Falta configurar RESEND_FROM."
            );
        }
    }

    private String buildHtml(String message) {
        String escaped = message
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");

        return """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto">
              <h2>MyComplex</h2>
              <p>%s</p>
              <hr/>
              <small>Correo enviado desde la PoC ADR-010.</small>
            </div>
            """.formatted(escaped);
    }
}
