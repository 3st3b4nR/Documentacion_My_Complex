# Evidencias sugeridas — PoC ADR-020

## Evidencia 1 — Azure Container Registry

Captura del recurso ACR mostrando:

```text
nombre
SKU
login server
```

No mostrar credenciales.

## Evidencia 2 — Repository

Captura de:

```text
Repositories
→ mycomplex-backend
```

## Evidencia 3 — Primera versión

Mostrar:

```text
1.0.0
```

como tag publicado.

## Evidencia 4 — Segunda versión

Publicar:

```text
1.0.1
```

y mostrar que:

```text
1.0.0
1.0.1
```

coexisten.

## Evidencia 5 — CLI

Ejecutar:

```powershell
.\scripts\show-images.ps1
```

y guardar la salida.

## Evidencia 6 — Pull

Ejecutar:

```text
docker pull <acr>.azurecr.io/mycomplex-backend:1.0.0
```

## Evidencia 7 — Ejecución

Ejecutar la imagen descargada y probar:

```text
GET /health
→ 200 OK
```

## Evidencia 8 — Registry privado

Mostrar que el admin user está deshabilitado y que la publicación se hizo mediante autenticación de Azure, sin convertir el repository en público.

## Conclusión sugerida

> La PoC confirmó que Azure Container Registry permite almacenar de forma centralizada las imágenes Docker del backend de MyComplex, conservar múltiples versiones mediante tags y recuperar posteriormente una imagen para su ejecución. El registry pudo mantenerse privado y no fue necesario administrar infraestructura propia para almacenar los artefactos. Esto demuestra la viabilidad técnica de la decisión del ADR-020.
