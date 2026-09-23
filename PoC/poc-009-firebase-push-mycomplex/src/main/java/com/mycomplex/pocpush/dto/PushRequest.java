package com.mycomplex.pocpush.dto;

import jakarta.validation.constraints.NotBlank;

public record PushRequest(
    @NotBlank String token,
    @NotBlank String title,
    @NotBlank String body
) {}
