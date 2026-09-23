# PoC ADR-020 — Azure Container Registry

## Objetivo

Esta prueba de concepto valida la decisión del ADR-020 de utilizar **Azure Container Registry (ACR)** para guardar y controlar las versiones de las imágenes Docker del backend de MyComplex.

El ADR establece que cada nueva versión empaquetada del backend necesita almacenarse en un repositorio central, conservar distintas versiones y permitir posteriormente que la plataforma de ejecución descargue las imágenes autorizadas. La opción seleccionada fue Azure Container Registry. fileciteturn30file0

---

## Flujo validado

```text
Código MyComplex
      ↓
Docker build
      ↓
imagen local
      ↓
docker tag
      ↓
Azure Container Registry
      ↓
repository + tags
      ↓
docker pull
      ↓
contenedor ejecutándose
```

---

## Qué demuestra

La PoC valida que:

1. Es posible crear un registry privado en Azure.
2. Una imagen Docker de MyComplex puede publicarse en ACR.
3. Las imágenes pueden organizarse en repositories.
4. El mismo repository puede conservar varios tags/versiones.
5. Es posible consultar las versiones disponibles.
6. Una imagen publicada puede descargarse posteriormente.
7. La imagen descargada puede ejecutarse correctamente.
8. No es necesario mantener un servidor propio para almacenar imágenes.

Esto responde directamente a los impulsores del ADR-020: almacenamiento central, historial de versiones, control de acceso e integración con el entorno de Azure. fileciteturn30file0

---

## Separación de ADR

Esta PoC prueba únicamente:

```text
ADR-020
¿Dónde guardamos las imágenes Docker?
```

No prueba:

```text
ADR-016 → cómo empaquetar el backend.
ADR-017 → dónde ejecutar el contenedor.
ADR-024 → Managed Identities y permisos entre servicios.
ADR-028 → automatización CI/CD.
```

Aunque Docker es necesario para generar una imagen de prueba, la decisión evaluada aquí es ACR.

---

## Estructura

```text
poc-020-azure-container-registry-mycomplex/
├── docs/
│   ├── azure-setup.md
│   └── evidence-checklist.md
├── scripts/
│   ├── build-tag-push.ps1
│   ├── check-prerequisites.ps1
│   ├── create-acr.ps1
│   ├── delete-acr.ps1
│   ├── pull-and-run.ps1
│   └── show-images.ps1
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocacr/
│       │   ├── PocAcrApplication.java
│       │   └── controller/
│       │       └── AcrController.java
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
Docker
suscripción Azure
```

---

## Paso 1 — Login

```powershell
az login
```

---

## Paso 2 — Verificar herramientas

```powershell
.\scripts\check-prerequisites.ps1
```

---

## Paso 3 — Crear ACR

```powershell
.\scripts\create-acr.ps1
```

El script crea un registry Basic con:

```text
admin user deshabilitado
```

para evitar depender de credenciales administrativas permanentes.

El nombre de ACR debe ser globalmente único.

Ejemplo:

```text
mycomplexacr1234
```

---

## Paso 4 — Configurar variables

Después de crear el registry:

```powershell
$env:AZURE_ACR_NAME="mycomplexacr1234"
$env:ACR_REPOSITORY="mycomplex-backend"
$env:IMAGE_TAG="1.0.0"
```

---

## Paso 5 — Construir y publicar

```powershell
.\scripts\build-tag-push.ps1
```

El proceso ejecuta:

```text
docker build
↓
docker tag
↓
az acr login
↓
docker push
```

La imagen final tendrá una dirección parecida a:

```text
mycomplexacr1234.azurecr.io/mycomplex-backend:1.0.0
```

---

## Paso 6 — Consultar imágenes

```powershell
.\scripts\show-images.ps1
```

Debe aparecer:

```text
mycomplex-backend
```

y el tag:

```text
1.0.0
```

---

## Probar versionado

Publica una segunda versión:

```powershell
$env:IMAGE_TAG="1.0.1"

.\scripts\build-tag-push.ps1
```

Luego:

```powershell
.\scripts\show-images.ps1
```

Esperado:

```text
1.0.1
1.0.0
```

Esto demuestra que ACR puede mantener varias versiones del backend, uno de los objetivos principales del ADR. fileciteturn30file0

---

## Descargar la imagen

Ejemplo:

```powershell
$env:IMAGE_TAG="1.0.0"

.\scripts\pull-and-run.ps1
```

El script realiza:

```text
az acr login
docker pull
docker run
```

y publica temporalmente el backend en:

```text
http://localhost:8089
```

---

## Verificar que funciona

En otra terminal:

```powershell
Invoke-RestMethod http://localhost:8089/health
```

Esperado:

```json
{
  "status": "UP",
  "service": "poc-020-acr"
}
```

Después:

```powershell
Invoke-RestMethod http://localhost:8089/api/version
```

Debe mostrar el tag utilizado.

---

## Control de acceso

Esta PoC utiliza:

```text
az acr login
```

con la identidad de tu sesión local de Azure.

Esto permite comprobar que ACR no necesita exponer una imagen públicamente.

La estrategia definitiva para que Azure Container Apps acceda al registry mediante Managed Identity corresponde a otro ADR y no se mezcla en esta PoC.

---

## Limpieza

Cuando termines:

```powershell
.\scripts\delete-acr.ps1
```

Así evitas mantener el registry de prueba activo innecesariamente.

---

## Qué NO valida

Esta PoC no prueba:

```text
- Azure Container Apps;
- Managed Identities;
- GitHub Actions;
- escaneo de vulnerabilidades;
- políticas avanzadas de retención;
- geo-replicación;
- producción.
```

El objetivo es únicamente demostrar que ACR puede almacenar, versionar y entregar las imágenes del backend.

---

## Criterios de éxito

La PoC se considera exitosa si:

```text
✓ se crea un Azure Container Registry;
✓ se publica una imagen Docker;
✓ ACR muestra el repository;
✓ ACR muestra el tag 1.0.0;
✓ se publica un segundo tag;
✓ ambas versiones permanecen disponibles;
✓ una imagen puede descargarse mediante docker pull;
✓ la imagen descargada puede ejecutarse;
✓ el registry permanece privado;
✓ no se necesita administrar un servidor propio de imágenes.
```

Si estas pruebas funcionan, se confirma la viabilidad técnica de Azure Container Registry para la decisión del ADR-020.
