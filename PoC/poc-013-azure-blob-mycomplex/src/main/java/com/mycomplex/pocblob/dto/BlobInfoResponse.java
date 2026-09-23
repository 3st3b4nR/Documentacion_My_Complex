package com.mycomplex.pocblob.dto;

public record BlobInfoResponse(
    String blobName,
    String contentType,
    long size,
    String etag
) {}
