package com.mycomplex.pocemail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resend")
public record ResendProperties(
    String apiKey,
    String from,
    String apiUrl
) {}
