package com.mycomplex.pocblob.controller;

import java.util.Map;

import com.mycomplex.pocblob.dto.BlobInfoResponse;
import com.mycomplex.pocblob.dto.BlobUploadResponse;
import com.mycomplex.pocblob.service.BlobStorageService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BlobController {

    private final BlobStorageService blobStorageService;

    public BlobController(BlobStorageService blobStorageService) {
        this.blobStorageService = blobStorageService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "poc-013-azure-blob"
        );
    }

    @PostMapping(
        value = "/api/tenants/{tenantId}/noticias/{noticiaId}/imagen",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BlobUploadResponse> upload(
            @PathVariable String tenantId,
            @PathVariable String noticiaId,
            @RequestParam("file") MultipartFile file) throws Exception {

        return ResponseEntity.ok(
            blobStorageService.upload(tenantId, noticiaId, file)
        );
    }

    @GetMapping(
        "/api/tenants/{tenantId}/noticias/{noticiaId}/imagen/{fileName}/info"
    )
    public ResponseEntity<BlobInfoResponse> info(
            @PathVariable String tenantId,
            @PathVariable String noticiaId,
            @PathVariable String fileName) {

        return ResponseEntity.ok(
            blobStorageService.info(tenantId, noticiaId, fileName)
        );
    }

    @GetMapping(
        "/api/tenants/{tenantId}/noticias/{noticiaId}/imagen/{fileName}"
    )
    public ResponseEntity<ByteArrayResource> download(
            @PathVariable String tenantId,
            @PathVariable String noticiaId,
            @PathVariable String fileName) {

        ByteArrayResource resource =
            blobStorageService.download(
                tenantId,
                noticiaId,
                fileName
            );

        String contentType =
            blobStorageService.contentType(
                tenantId,
                noticiaId,
                fileName
            );

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + fileName + "\""
            )
            .body(resource);
    }

    @DeleteMapping(
        "/api/tenants/{tenantId}/noticias/{noticiaId}/imagen/{fileName}"
    )
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable String tenantId,
            @PathVariable String noticiaId,
            @PathVariable String fileName) {

        blobStorageService.delete(
            tenantId,
            noticiaId,
            fileName
        );

        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "message", "Imagen eliminada correctamente"
            )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> badRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
            Map.of(
                "success", false,
                "error", ex.getMessage()
            )
        );
    }
}
