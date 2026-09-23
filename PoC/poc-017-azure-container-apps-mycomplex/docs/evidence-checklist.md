# Evidencias sugeridas — PoC ADR-017

## Evidencia 1 — Resource Group

Captura del grupo:

```text
rg-mycomplex-poc
```

## Evidencia 2 — Container Apps Environment

Captura de:

```text
mycomplex-poc-env
```

## Evidencia 3 — Container App

Captura de:

```text
mycomplex-poc-017
```

mostrando:

```text
Running / Succeeded
FQDN
revisión activa
```

## Evidencia 4 — Health

Abrir:

```text
https://FQDN/health
```

Esperado:

```text
200 OK
```

## Evidencia 5 — Info

Abrir:

```text
https://FQDN/api/info
```

Debe mostrar:

```text
environment = azure-container-apps
```

## Evidencia 6 — Revisión

Ejecutar:

```powershell
.\scripts\show-status.ps1
```

y guardar evidencia de la revisión activa.

## Evidencia 7 — Actualización

Publicar una segunda versión de imagen y ejecutar de nuevo:

```powershell
.\scripts\deploy.ps1
```

Comprobar que se crea/actualiza la revisión sin configurar una VM manualmente.

## Evidencia 8 — Eliminación

Al finalizar:

```powershell
.\scripts\delete-resources.ps1
```

## Conclusión sugerida

> La PoC confirmó que Azure Container Apps puede ejecutar el backend contenedorizado de MyComplex, exponerlo mediante una URL HTTPS, recibir configuración externa y administrar revisiones sin requerir que el equipo configure servidores o un clúster Kubernetes. Esto demuestra la viabilidad técnica de la decisión tomada en el ADR-017.
