package com.mycomplex.pocemail.dto;

public record EmailResponse(
    boolean success,
    String providerId,
    int providerStatus,
    String message
) {}
