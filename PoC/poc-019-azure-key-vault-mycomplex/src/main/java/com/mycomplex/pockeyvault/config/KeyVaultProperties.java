package com.mycomplex.pockeyvault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.keyvault")
public record KeyVaultProperties(
    String url
) {}
