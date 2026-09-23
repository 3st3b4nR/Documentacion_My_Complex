package com.mycomplex.pocwompi;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class WebhookServiceTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void processesRepeatedEventOnlyOnce() throws Exception {
        JsonNode event = mapper.readTree("""
            {"data":{"transaction":{"id":"tx-001","status":"APPROVED"}},"signature":{"checksum":"event-001"}}
            """);
        WebhookService service = new WebhookService();

        assertThat(service.process(event)).isTrue();
        assertThat(service.process(event)).isFalse();
        assertThat(service.statusOf("tx-001")).isEqualTo("APPROVED");
    }
}

