package com.mycomplex.pocblob.dto;

public record BlobUploadResponse(
    boolean success,
    String tenantId,
    String noticiaId,
    String blobName,
    String contentType,
    long size,
    String message
) {}
