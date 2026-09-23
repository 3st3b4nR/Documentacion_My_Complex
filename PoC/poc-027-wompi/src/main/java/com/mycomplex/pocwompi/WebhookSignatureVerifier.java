package com.mycomplex.pocwompi;

import com.fasterxml.jackson.databind.JsonNode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.stereotype.Component;

@Component
public class WebhookSignatureVerifier {
    public String calculate(JsonNode event, String secret) {
        StringBuilder source = new StringBuilder();
        JsonNode data = event.path("data");
        for (JsonNode property : event.path("signature").path("properties")) {
            JsonNode value = resolve(data, property.asText());
            if (value.isMissingNode() || value.isNull()) {
                throw new IllegalArgumentException("Propiedad de firma ausente: " + property.asText());
            }
            source.append(value.asText());
        }
        source.append(event.path("timestamp").asText());
        source.append(secret);
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(source.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no disponible", exception);
        }
    }

    public boolean isValid(JsonNode event, String headerChecksum, String secret) {
        String expected = calculate(event, secret);
        String bodyChecksum = event.path("signature").path("checksum").asText();
        return constantTimeEquals(expected, headerChecksum) && constantTimeEquals(expected, bodyChecksum);
    }

    private JsonNode resolve(JsonNode root, String dottedPath) {
        JsonNode current = root;
        for (String segment : dottedPath.split("\\.")) current = current.path(segment);
        return current;
    }

    private boolean constantTimeEquals(String left, String right) {
        if (left == null || right == null) return false;
        return MessageDigest.isEqual(left.toLowerCase().getBytes(StandardCharsets.UTF_8), right.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}

