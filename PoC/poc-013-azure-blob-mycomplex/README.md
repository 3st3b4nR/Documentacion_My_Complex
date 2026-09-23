# PoC ADR-013 — Azure Blob Storage para imágenes de noticias

## Objetivo

Esta prueba de concepto valida la decisión del ADR-013 de utilizar **Azure Blob Storage mediante una Storage Account** para almacenar las imágenes asociadas a las noticias de MyComplex.

El ADR establece que las imágenes deben almacenarse fuera de PostgreSQL y organizarse de manera que puedan mantenerse separadas por conjunto residencial y noticia. fileciteturn26file0

La PoC demuestra este flujo:

```text
Administrador
     ↓
Backend Spring Boot
     ↓
valida imagen
     ↓
Azure Blob Storage
     ↓
tenants/{tenantId}/noticias/{noticiaId}/archivo
```

---

## Qué demuestra

La PoC valida que:

1. Spring Boot puede conectarse a Azure Blob Storage.
2. Una imagen puede almacenarse fuera de PostgreSQL.
3. Los archivos pueden organizarse lógicamente por `TenantId`.
4. Cada imagen puede asociarse a una noticia concreta.
5. Se pueden consultar los metadatos del archivo.
6. Se puede descargar una imagen almacenada.
7. Se puede eliminar una imagen.
8. El backend puede limitar tamaño y tipo de archivo antes de guardarlo.

---

## Organización de los blobs

Los archivos se guardan con esta estructura:

```text
tenants/
└── conjunto-001/
    └── noticias/
        └── noticia-100/
            └── UUID.jpg
```

Ejemplo:

```text
tenants/conjunto-001/noticias/noticia-100/8af3...c12.jpg
```

Esto permite conservar una separación lógica por conjunto residencial, tal como plantea el ADR. fileciteturn26file0

---

## Importante sobre seguridad multi-tenant

Esta PoC demuestra la **organización del almacenamiento por TenantId**.

No vuelve a implementar la autorización del ADR-007.

En MyComplex real:

```text
JWT
 ↓
backend valida TenantId
 ↓
backend construye ruta del blob
 ↓
Azure Blob Storage
```

Por tanto, el usuario nunca debería poder elegir libremente un TenantId que no le pertenece.

---

## Estructura

```text
poc-013-azure-blob-mycomplex/
├── docs/
│   ├── azure-setup.md
│   └── evidence-checklist.md
├── examples/
│   └── README.md
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocblob/
│       │   ├── PocBlobApplication.java
│       │   ├── config/
│       │   │   ├── BlobProperties.java
│       │   │   └── BlobStorageConfig.java
│       │   ├── controller/
│       │   │   └── BlobController.java
│       │   ├── dto/
│       │   │   ├── BlobInfoResponse.java
│       │   │   └── BlobUploadResponse.java
│       │   └── service/
│       │       └── BlobStorageService.java
│       └── resources/
│           └── application.properties
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Límites aplicados

Para esta PoC:

```text
Tamaño máximo: 5 MB

Tipos permitidos:
- image/jpeg
- image/png
- image/webp
```

Esto responde a la necesidad del ADR de controlar tamaño y tipo de los archivos publicados. fileciteturn26file0

---

## Configuración

Consulta:

```text
docs/azure-setup.md
```

Necesitas:

```text
Azure Storage Account
+
Blob Container
+
Connection String
```

La credencial no se escribe en Java.

Se suministra mediante:

```text
AZURE_STORAGE_CONNECTION_STRING
```

---

## Ejecutar

PowerShell:

```powershell
$env:AZURE_STORAGE_CONNECTION_STRING="TU_CONNECTION_STRING"
$env:AZURE_STORAGE_CONTAINER="mycomplex-noticias"

mvn spring-boot:run
```

La PoC usa:

```text
http://localhost:8085
```

---

## Probar estado

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8085/health"
```

Esperado:

```json
{
  "status": "UP",
  "service": "poc-013-azure-blob"
}
```

---

## Subir imagen

Ejemplo con `curl.exe` en PowerShell:

```powershell
curl.exe -X POST `
  "http://localhost:8085/api/tenants/conjunto-001/noticias/noticia-100/imagen" `
  -F "file=@C:\ruta\foto-prueba.jpg"
```

Respuesta esperada:

```json
{
  "success": true,
  "tenantId": "conjunto-001",
  "noticiaId": "noticia-100",
  "blobName": "tenants/conjunto-001/noticias/noticia-100/UUID.jpg",
  "contentType": "image/jpeg",
  "size": 123456,
  "message": "Imagen almacenada correctamente en Azure Blob Storage"
}
```

Guarda el nombre del archivo final:

```text
UUID.jpg
```

---

## Consultar metadatos

```powershell
Invoke-RestMethod `
  "http://localhost:8085/api/tenants/conjunto-001/noticias/noticia-100/imagen/UUID.jpg/info"
```

Esperado:

```json
{
  "blobName": "tenants/conjunto-001/noticias/noticia-100/UUID.jpg",
  "contentType": "image/jpeg",
  "size": 123456,
  "etag": "..."
}
```

---

## Descargar

Abre:

```text
http://localhost:8085/api/tenants/conjunto-001/noticias/noticia-100/imagen/UUID.jpg
```

La imagen debe mostrarse en el navegador.

---

## Eliminar

```powershell
Invoke-RestMethod `
  -Method Delete `
  "http://localhost:8085/api/tenants/conjunto-001/noticias/noticia-100/imagen/UUID.jpg"
```

Esperado:

```json
{
  "success": true,
  "message": "Imagen eliminada correctamente"
}
```

---

## Qué NO valida

Esta PoC no prueba:

- autorización con Auth0;
- persistencia de la noticia en PostgreSQL;
- Managed Identities;
- Key Vault;
- WAF;
- Docker;
- CDN;
- CI/CD.

El propósito es únicamente demostrar que Azure Blob Storage funciona correctamente como almacenamiento de las imágenes del portal de noticias.

---

## Criterio de éxito

```text
✓ Spring Boot se conecta a Azure Blob Storage.
✓ Se crea o utiliza el contenedor configurado.
✓ Una imagen válida puede almacenarse.
✓ La ruta contiene TenantId y noticiaId.
✓ Una imagen mayor de 5 MB es rechazada.
✓ Un archivo que no sea JPEG, PNG o WEBP es rechazado.
✓ La imagen puede consultarse y descargarse.
✓ La imagen puede eliminarse.
✓ PostgreSQL no almacena el archivo binario.
```

Si estas pruebas funcionan, se confirma la viabilidad técnica de Azure Blob Storage para el ADR-013.
