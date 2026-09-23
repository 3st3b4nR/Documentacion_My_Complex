# PoC ADR-0020/0021/0023/0024 — Infraestructura Azure

## Objetivo

Validar de manera integrada que una imagen Docker de MyComplex puede almacenarse en Azure Container Registry, desplegarse en Azure Container Apps, acceder a recursos mediante identidad administrada y conectarse a PostgreSQL mediante TLS desde un origen permitido.

## Decisiones cubiertas

* ADR-0020: Azure Container Registry.
* ADR-0021: Azure Database for PostgreSQL Flexible Server y recuperación administrada.
* ADR-0023: firewall con acceso público restringido y TLS.
* ADR-0024: Managed Identity y RBAC.

## Dependencias anteriores

* ADR-0013: Blob Storage es uno de los recursos protegidos por RBAC.
* ADR-0016: se necesita una imagen Docker para publicar en ACR.
* ADR-0017: Container Apps ejecuta la imagen y proporciona la identidad.
* ADR-0019: Key Vault conserva secretos que todavía sean necesarios.

## Hipótesis

1. Container Apps puede descargar una imagen privada de ACR mediante identidad administrada.
2. La identidad del backend puede leer solo el secreto y el contenedor Blob autorizados.
3. PostgreSQL rechaza un origen no permitido y acepta el origen habilitado usando TLS.
4. Una restauración administrada crea una instancia utilizable sin alterar el servidor original.

## Ejecución

1. Instalar Azure CLI y autenticarse en una suscripción de pruebas.
2. Copiar `config.example.psd1` como `config.local.psd1` y completar nombres únicos. No confirmar ese archivo.
3. Ejecutar `scripts/deploy.ps1` desde PowerShell 7.
4. Ejecutar `scripts/verify.ps1` y completar `docs/evidence-checklist.md`.
5. Ejecutar una restauración a un momento anterior desde Azure y documentar el servidor restaurado.
6. Eliminar el grupo de recursos al terminar para detener costos.

## Criterio de aceptación

La PoC se acepta si la imagen versionada se despliega, la identidad puede leer únicamente los recursos autorizados, un origen no permitido es rechazado, la conexión aceptada usa TLS y la restauración conserva los datos de control. Ninguna credencial debe aparecer en el repositorio o en la salida guardada como evidencia.

## Estado

La automatización y la lista de evidencia están preparadas. La PoC permanece **pendiente de ejecución** porque requiere una suscripción Azure, cuotas disponibles, una imagen de aplicación y aprobación de los costos temporales.

