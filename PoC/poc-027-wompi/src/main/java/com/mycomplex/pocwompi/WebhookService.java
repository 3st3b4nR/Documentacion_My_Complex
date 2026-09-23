package com.mycomplex.pocwompi;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {
    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, String> transactionStatuses = new ConcurrentHashMap<>();

    public boolean process(JsonNode event) {
        String checksum = event.path("signature").path("checksum").asText();
        if (!processedEvents.add(checksum)) return false;
        JsonNode transaction = event.path("data").path("transaction");
        transactionStatuses.put(transaction.path("id").asText(), transaction.path("status").asText());
        return true;
    }

    public String statusOf(String transactionId) {
        return transactionStatuses.get(transactionId);
    }
}

