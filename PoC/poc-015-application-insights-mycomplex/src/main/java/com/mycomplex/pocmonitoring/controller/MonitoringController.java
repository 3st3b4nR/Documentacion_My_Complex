package com.mycomplex.pocmonitoring.controller;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MonitoringController {

    private static final Logger logger =
        LoggerFactory.getLogger(MonitoringController.class);

    @GetMapping("/health")
    public Map<String, Object> health() {
        logger.info("Health check ejecutado");

        return Map.of(
            "status", "UP",
            "service", "poc-015-application-insights",
            "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/api/work")
    public Map<String, Object> work(
            @RequestParam(defaultValue = "250") long delayMs)
            throws InterruptedException {

        long safeDelay = Math.max(0, Math.min(delayMs, 5000));

        logger.info(
            "Iniciando operacion de prueba con delay={}ms",
            safeDelay
        );

        Thread.sleep(safeDelay);

        logger.info(
            "Operacion de prueba finalizada correctamente"
        );

        return Map.of(
            "success", true,
            "delayMs", safeDelay,
            "message", "Operacion completada"
        );
    }

    @GetMapping("/api/random")
    public Map<String, Object> randomWork()
            throws InterruptedException {

        int delay =
            ThreadLocalRandom.current().nextInt(50, 1200);

        Thread.sleep(delay);

        logger.info(
            "Solicitud aleatoria completada en {}ms",
            delay
        );

        return Map.of(
            "delayMs", delay,
            "message", "Solicitud para generar telemetria"
        );
    }

    @GetMapping("/api/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void generateError() {
        logger.error(
            "Error intencional generado por la PoC ADR-015"
        );

        throw new IllegalStateException(
            "Error intencional para validar Application Insights"
        );
    }
}
