package com.mycomplex.pocwompi;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class WebhookSignatureVerifierTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final WebhookSignatureVerifier verifier = new WebhookSignatureVerifier();

    @Test
    void validatesOfficialConcatenationOrderAndRejectsTampering() throws Exception {
        String checksum = "5391209a2fdccc21953e008489a57fae3c2ea5158bdd380314c7cb2d72f6890f";
        JsonNode event = mapper.readTree("""
            {"data":{"transaction":{"id":"tx-001","status":"APPROVED","amount_in_cents":50000}},
             "timestamp":1700000000,
             "signature":{"properties":["transaction.id","transaction.status","transaction.amount_in_cents"],"checksum":"%s"}}
            """.formatted(checksum));

        assertThat(verifier.isValid(event, checksum, "test_events_secret")).isTrue();
        ((com.fasterxml.jackson.databind.node.ObjectNode) event.path("data").path("transaction")).put("amount_in_cents", 90000);
        assertThat(verifier.isValid(event, checksum, "test_events_secret")).isFalse();
    }
}

