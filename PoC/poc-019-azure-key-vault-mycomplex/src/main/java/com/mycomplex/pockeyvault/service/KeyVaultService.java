package com.mycomplex.pockeyvault.service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;
import com.mycomplex.pockeyvault.dto.SecretMetadataResponse;
import com.mycomplex.pockeyvault.dto.SecretResponse;

import org.springframework.stereotype.Service;

@Service
public class KeyVaultService {

    private final SecretClient secretClient;

    public KeyVaultService(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    public SecretResponse save(String name, String value) {
        validateName(name);

        KeyVaultSecret secret =
            secretClient.setSecret(name, value);

        return new SecretResponse(
            true,
            secret.getName(),
            secret.getProperties().getVersion(),
            "Secreto almacenado correctamente en Azure Key Vault"
        );
    }

    public SecretMetadataResponse metadata(String name) {
        validateName(name);

        KeyVaultSecret secret = secretClient.getSecret(name);
        SecretProperties properties = secret.getProperties();

        return new SecretMetadataResponse(
            secret.getName(),
            properties.getVersion(),
            Boolean.TRUE.equals(properties.isEnabled()),
            properties.getCreatedOn() == null
                ? null
                : properties.getCreatedOn().toString(),
            properties.getUpdatedOn() == null
                ? null
                : properties.getUpdatedOn().toString()
        );
    }

    public SecretResponse verify(String name, String expectedValue) {
        validateName(name);

        KeyVaultSecret secret = secretClient.getSecret(name);

        boolean matches =
            secret.getValue() != null &&
            secret.getValue().equals(expectedValue);

        return new SecretResponse(
            matches,
            secret.getName(),
            secret.getProperties().getVersion(),
            matches
                ? "El valor leído desde Key Vault coincide con el esperado"
                : "El valor leído desde Key Vault NO coincide con el esperado"
        );
    }

    private void validateName(String name) {
        if (name == null ||
            !name.matches("[A-Za-z0-9-]{1,127}")) {

            throw new IllegalArgumentException(
                "Nombre de secreto inválido. Use letras, números y guiones."
            );
        }
    }
}
