# Evidencias sugeridas — PoC ADR-016

## Evidencia 1 — Docker instalado

```text
docker --version
```

Captura del resultado.

## Evidencia 2 — Construcción

```text
docker build ...
```

Captura indicando que la imagen fue creada.

## Evidencia 3 — Imagen

```text
docker images
```

Debe aparecer:

```text
mycomplex/poc-016-docker
```

con tag:

```text
1.0.0
```

## Evidencia 4 — Contenedor activo

```text
docker ps
```

Debe aparecer:

```text
mycomplex-poc-016
```

## Evidencia 5 — Health

```text
GET http://localhost:8087/health
→ 200 OK
```

## Evidencia 6 — Ejecución dentro del contenedor

```text
GET http://localhost:8087/api/info
```

Guardar evidencia de:

```text
hostname
environment
javaVersion
```

## Evidencia 7 — Configuración externa

Ejecutar la misma imagen con:

```text
APP_ENVIRONMENT=pruebas
```

y demostrar que cambia la respuesta sin reconstruir la imagen.

## Evidencia 8 — Reutilización

Detener/eliminar el contenedor y volver a ejecutar la misma imagen.

## Conclusión sugerida

> La PoC confirmó que el backend Spring Boot de MyComplex puede empaquetarse en una imagen Docker y ejecutarse de manera aislada y reproducible. La misma imagen pudo reutilizarse sin modificar el artefacto, mientras la configuración del entorno se proporcionó externamente mediante variables de entorno. Esto demuestra la viabilidad técnica de Docker para la decisión del ADR-016.
