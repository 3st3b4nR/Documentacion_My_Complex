package com.mycomplex.pockeyvault.dto;

public record SecretResponse(
    boolean success,
    String name,
    String version,
    String message
) {}
