package com.mycomplex.pocblob.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BlobProperties.class)
public class BlobStorageConfig {

    @Bean
    BlobContainerClient blobContainerClient(BlobProperties properties) {
        if (properties.connectionString() == null ||
            properties.connectionString().isBlank()) {
            throw new IllegalStateException(
                "Falta configurar AZURE_STORAGE_CONNECTION_STRING."
            );
        }

        String containerName = properties.containerName();

        if (containerName == null || containerName.isBlank()) {
            throw new IllegalStateException(
                "Falta configurar AZURE_STORAGE_CONTAINER."
            );
        }

        BlobContainerClient client =
            new BlobContainerClientBuilder()
                .connectionString(properties.connectionString())
                .containerName(containerName)
                .buildClient();

        client.createIfNotExists();

        return client;
    }
}
