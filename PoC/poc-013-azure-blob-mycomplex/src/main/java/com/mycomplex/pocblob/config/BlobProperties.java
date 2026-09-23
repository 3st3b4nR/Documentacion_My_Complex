package com.mycomplex.pocblob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.storage")
public record BlobProperties(
    String connectionString,
    String containerName
) {}
