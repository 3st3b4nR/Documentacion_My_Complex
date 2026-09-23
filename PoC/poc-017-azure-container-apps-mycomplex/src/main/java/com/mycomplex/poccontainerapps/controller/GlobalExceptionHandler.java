package com.mycomplex.poccontainerapps.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handle(
            IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Map.of(
                    "status", 500,
                    "message", ex.getMessage(),
                    "timestamp", Instant.now().toString()
                )
            );
    }
}
