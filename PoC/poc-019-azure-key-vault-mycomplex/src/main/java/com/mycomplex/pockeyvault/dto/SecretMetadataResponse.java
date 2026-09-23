package com.mycomplex.pockeyvault.dto;

public record SecretMetadataResponse(
    String name,
    String version,
    boolean enabled,
    String createdOn,
    String updatedOn
) {}
