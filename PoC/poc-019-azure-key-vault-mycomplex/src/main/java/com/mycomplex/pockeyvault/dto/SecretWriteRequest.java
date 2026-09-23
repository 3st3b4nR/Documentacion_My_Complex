package com.mycomplex.pockeyvault.dto;

import jakarta.validation.constraints.NotBlank;

public record SecretWriteRequest(
    @NotBlank String name,
    @NotBlank String value
) {}
