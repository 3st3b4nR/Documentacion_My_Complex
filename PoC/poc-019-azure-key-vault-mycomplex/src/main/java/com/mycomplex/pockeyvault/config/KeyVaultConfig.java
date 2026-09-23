package com.mycomplex.pockeyvault.config;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeyVaultProperties.class)
public class KeyVaultConfig {

    @Bean
    SecretClient secretClient(KeyVaultProperties properties) {

        if (properties.url() == null || properties.url().isBlank()) {
            throw new IllegalStateException(
                "Falta configurar AZURE_KEY_VAULT_URL."
            );
        }

        return new SecretClientBuilder()
            .vaultUrl(properties.url())
            .credential(
                new DefaultAzureCredentialBuilder().build()
            )
            .buildClient();
    }
}
