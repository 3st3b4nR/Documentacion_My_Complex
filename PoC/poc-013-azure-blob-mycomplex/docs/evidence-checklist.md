# Evidencias sugeridas — PoC ADR-013

## Evidencia 1 — Storage Account

Captura del contenedor:

```text
mycomplex-noticias
```

No muestres Access Keys ni Connection Strings.

## Evidencia 2 — Carga exitosa

```text
POST /api/tenants/conjunto-001/noticias/noticia-100/imagen
→ 200 OK
```

La respuesta debe mostrar un `blobName`.

## Evidencia 3 — Organización por TenantId

En Azure Portal debe observarse una ruta equivalente a:

```text
tenants/conjunto-001/noticias/noticia-100/...
```

## Evidencia 4 — Metadatos

```text
GET .../info
→ contentType
→ size
→ etag
```

## Evidencia 5 — Descarga

Abrir el endpoint de descarga y mostrar la imagen.

## Evidencia 6 — Validación

Intentar subir:

```text
archivo .txt
```

Resultado esperado:

```text
400 Bad Request
```

## Evidencia 7 — Eliminación

```text
DELETE ...
→ 200 OK
```

y verificar en Azure que el blob desapareció.

## Conclusión sugerida

> La PoC confirmó que Spring Boot puede almacenar, consultar, descargar y eliminar imágenes en Azure Blob Storage sin guardar los archivos binarios en PostgreSQL. Los blobs se organizaron mediante rutas que incorporan TenantId y noticiaId, demostrando la viabilidad técnica de separar el almacenamiento de imágenes por conjunto residencial según la decisión del ADR-013.
