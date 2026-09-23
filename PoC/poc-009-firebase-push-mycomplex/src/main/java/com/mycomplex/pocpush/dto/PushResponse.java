package com.mycomplex.pocpush.dto;

public record PushResponse(
    boolean success,
    String messageId,
    String message
) {}
