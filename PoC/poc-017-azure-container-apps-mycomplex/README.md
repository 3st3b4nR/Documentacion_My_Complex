# PoC ADR-017 — Azure Container Apps

## Objetivo

Esta prueba de concepto valida la decisión del ADR-017 de utilizar **Azure Container Apps** para ejecutar el backend contenedorizado de MyComplex en la nube.

El ADR plantea que, después de empaquetar el backend con Docker, MyComplex necesita una plataforma capaz de ejecutar el contenedor, mantenerlo disponible y permitir ampliar o reducir instancias sin que el equipo tenga que administrar directamente servidores o un clúster Kubernetes. La opción seleccionada fue Azure Container Apps. fileciteturn28file3

---

## Flujo validado

```text
Backend Spring Boot
       ↓
Docker image
       ↓
Registry accesible
       ↓
Azure Container Apps
       ↓
URL HTTPS pública
       ↓
/health
/api/info
```

---

## Qué demuestra

La PoC permite comprobar que:

1. Azure Container Apps puede ejecutar una imagen Docker de MyComplex.
2. El equipo no necesita crear ni administrar una máquina virtual.
3. El backend obtiene una URL pública administrada.
4. El contenedor puede recibir configuración mediante variables de entorno.
5. Azure mantiene una revisión desplegada de la aplicación.
6. Se pueden definir valores mínimo y máximo de réplicas.
7. Una nueva imagen puede desplegarse mediante actualización de la Container App.
8. La plataforma expone información operativa sobre revisiones y réplicas.

Esto corresponde a la necesidad descrita en el ADR-017 de reducir el trabajo operativo y disponer de una plataforma adecuada para el backend modular actual. fileciteturn28file3

---

## Importante: separación de ADR

Esta PoC prueba:

```text
ADR-017
¿Dónde y cómo ejecutamos el contenedor?
```

No prueba:

```text
ADR-016 → cómo se construye/empaqueta la imagen.
ADR-018 → cómo se distribuye tráfico entre varias réplicas.
ADR-020 → qué registry almacena las imágenes.
ADR-028 → qué herramienta automatiza CI/CD.
```

Por eso el script recibe simplemente:

```text
CONTAINER_IMAGE
```

La imagen puede proceder de cualquier registry que Azure pueda consultar.

No estamos utilizando esta PoC para decidir el registry.

---

## Estructura

```text
poc-017-azure-container-apps-mycomplex/
├── docs/
│   ├── azure-setup.md
│   └── evidence-checklist.md
├── scripts/
│   ├── check-prerequisites.ps1
│   ├── deploy.ps1
│   ├── delete-resources.ps1
│   ├── show-status.ps1
│   └── smoke-test.ps1
├── src/
│   └── main/
│       ├── java/com/mycomplex/poccontainerapps/
│       │   ├── PocContainerAppsApplication.java
│       │   └── controller/
│       │       ├── ContainerAppsController.java
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties
├── .dockerignore
├── .env.example
├── .gitignore
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Requisitos

Necesitas:

```text
Azure CLI
una cuenta/suscripción de Azure
una imagen Docker publicada en un registry accesible
```

Esta PoC no crea Azure Container Registry, porque esa decisión corresponde al ADR-020.

---

## Paso 1 — Iniciar sesión

```powershell
az login
```

Comprueba:

```powershell
az account show
```

---

## Paso 2 — Configurar variables

Ejemplo:

```powershell
$env:AZURE_RESOURCE_GROUP="rg-mycomplex-poc"
$env:AZURE_LOCATION="eastus"
$env:AZURE_CONTAINERAPPS_ENV="mycomplex-poc-env"
$env:AZURE_CONTAINERAPP_NAME="mycomplex-poc-017"

$env:CONTAINER_IMAGE="TU_REGISTRY/TU_IMAGEN:1.0.0"
```

---

## Paso 3 — Verificar prerequisitos

```powershell
.\scripts\check-prerequisites.ps1
```

---

## Paso 4 — Desplegar

```powershell
.\scripts\deploy.ps1
```

El script crea:

```text
Resource Group
      ↓
Container Apps Environment
      ↓
Container App
      ↓
Ingress externo
      ↓
HTTPS
```

El contenedor escucha internamente en:

```text
8080
```

---

## Paso 5 — Probar

Al finalizar, Azure mostrará una URL parecida a:

```text
https://mycomplex-poc-017.xxxxx.azurecontainerapps.io
```

Prueba:

```text
/health
```

y:

```text
/api/info
```

También puedes ejecutar:

```powershell
.\scripts\smoke-test.ps1 `
  -BaseUrl "https://TU-URL.azurecontainerapps.io"
```

---

## Resultado de `/api/info`

Debe verse algo parecido a:

```json
{
  "application": "MyComplex",
  "poc": "ADR-017 Azure Container Apps",
  "environment": "azure-container-apps",
  "hostname": "...",
  "javaVersion": "21...",
  "timestamp": "..."
}
```

La presencia de:

```text
environment = azure-container-apps
```

demuestra que Azure entregó correctamente la configuración al contenedor.

---

## Escalabilidad

La PoC configura:

```text
min replicas = 0
max replicas = 2
```

Esto demuestra que la plataforma admite un rango de escalado sin obligar al equipo a administrar servidores.

El análisis detallado de cómo se distribuye tráfico entre múltiples réplicas corresponde a la PoC del ADR-018.

---

## Actualizar la imagen

Si publicas una nueva versión:

```text
TU_REGISTRY/TU_IMAGEN:1.0.1
```

actualiza:

```powershell
$env:CONTAINER_IMAGE="TU_REGISTRY/TU_IMAGEN:1.0.1"
.\scripts\deploy.ps1
```

El script detecta que la aplicación ya existe y ejecuta una actualización.

---

## Consultar estado

```powershell
.\scripts\show-status.ps1
```

Muestra:

```text
nombre
FQDN
estado de aprovisionamiento
última revisión
réplicas
```

---

## Limpieza

Para evitar dejar recursos activos:

```powershell
.\scripts\delete-resources.ps1
```

El script exige escribir:

```text
ELIMINAR
```

antes de eliminar el Resource Group.

---

## Qué NO valida esta PoC

No se valida:

```text
- ACR como registry definitivo;
- balanceo avanzado;
- Cloudflare WAF;
- Key Vault;
- Managed Identities;
- PostgreSQL;
- GitHub Actions;
- dominio personalizado;
- monitoreo definitivo.
```

Cada punto pertenece a otra decisión arquitectónica.

---

## Criterios de éxito

La PoC se considera exitosa si:

```text
✓ Azure Container Apps crea correctamente la aplicación.
✓ la imagen Docker puede ejecutarse en Azure.
✓ /health devuelve 200.
✓ /api/info responde desde la nube.
✓ Azure proporciona un FQDN HTTPS.
✓ las variables de entorno llegan al contenedor.
✓ se observa una revisión activa.
✓ puede actualizarse la imagen sin crear manualmente una VM.
✓ la plataforma admite configuración de réplicas sin administrar Kubernetes.
```

Si se cumplen estos puntos, se confirma la viabilidad técnica de Azure Container Apps para la decisión del ADR-017.
