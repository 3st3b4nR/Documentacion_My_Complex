package com.mycomplex.pocblob.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobProperties;
import com.mycomplex.pocblob.dto.BlobInfoResponse;
import com.mycomplex.pocblob.dto.BlobUploadResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BlobStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of(
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        "image/webp"
    );

    private final BlobContainerClient containerClient;

    public BlobStorageService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public BlobUploadResponse upload(
            String tenantId,
            String noticiaId,
            MultipartFile file) throws IOException {

        validateIdentifier("tenantId", tenantId);
        validateIdentifier("noticiaId", noticiaId);
        validateFile(file);

        String extension = extensionFor(file.getContentType());

        String blobName = String.format(
            "tenants/%s/noticias/%s/%s%s",
            tenantId,
            noticiaId,
            UUID.randomUUID(),
            extension
        );

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try (ByteArrayInputStream input =
                 new ByteArrayInputStream(file.getBytes())) {

            blobClient.upload(input, file.getSize(), true);
        }

        blobClient.setHttpHeaders(
            new BlobHttpHeaders()
                .setContentType(file.getContentType())
                .setCacheControl("private, max-age=3600")
        );

        return new BlobUploadResponse(
            true,
            tenantId,
            noticiaId,
            blobName,
            file.getContentType(),
            file.getSize(),
            "Imagen almacenada correctamente en Azure Blob Storage"
        );
    }

    public BlobInfoResponse info(
            String tenantId,
            String noticiaId,
            String fileName) {

        String blobName = buildExistingBlobName(
            tenantId,
            noticiaId,
            fileName
        );

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new IllegalArgumentException("El blob no existe.");
        }

        BlobProperties properties = blobClient.getProperties();

        return new BlobInfoResponse(
            blobName,
            properties.getContentType(),
            properties.getBlobSize(),
            properties.getETag()
        );
    }

    public ByteArrayResource download(
            String tenantId,
            String noticiaId,
            String fileName) {

        String blobName = buildExistingBlobName(
            tenantId,
            noticiaId,
            fileName
        );

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new IllegalArgumentException("El blob no existe.");
        }

        byte[] bytes = blobClient.downloadContent().toBytes();

        return new ByteArrayResource(bytes);
    }

    public String contentType(
            String tenantId,
            String noticiaId,
            String fileName) {

        String blobName = buildExistingBlobName(
            tenantId,
            noticiaId,
            fileName
        );

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new IllegalArgumentException("El blob no existe.");
        }

        return blobClient.getProperties().getContentType();
    }

    public void delete(
            String tenantId,
            String noticiaId,
            String fileName) {

        String blobName = buildExistingBlobName(
            tenantId,
            noticiaId,
            fileName
        );

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new IllegalArgumentException("El blob no existe.");
        }

        blobClient.delete();
    }

    private String buildExistingBlobName(
            String tenantId,
            String noticiaId,
            String fileName) {

        validateIdentifier("tenantId", tenantId);
        validateIdentifier("noticiaId", noticiaId);
        validateFileName(fileName);

        return String.format(
            "tenants/%s/noticias/%s/%s",
            tenantId,
            noticiaId,
            fileName
        );
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                "Debe enviar una imagen."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                "La imagen supera el tamaño máximo permitido de 5 MB."
            );
        }

        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                "Solo se permiten imágenes JPEG, PNG o WEBP."
            );
        }
    }

    private void validateIdentifier(String field, String value) {
        if (value == null || !value.matches("[A-Za-z0-9_-]{1,80}")) {
            throw new IllegalArgumentException(
                field + " contiene caracteres no permitidos."
            );
        }
    }

    private void validateFileName(String value) {
        if (value == null ||
            !value.matches("[A-Za-z0-9._-]{1,150}") ||
            value.contains("..")) {
            throw new IllegalArgumentException(
                "Nombre de archivo no válido."
            );
        }
    }

    private String extensionFor(String contentType) {
        return switch (contentType) {
            case MediaType.IMAGE_JPEG_VALUE -> ".jpg";
            case MediaType.IMAGE_PNG_VALUE -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}
