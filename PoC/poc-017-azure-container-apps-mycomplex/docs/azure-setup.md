# Configuración Azure — PoC ADR-017

## 1. Cuenta

Necesitas una suscripción Azure activa.

Para estudiantes puede utilizarse una suscripción académica cuando esté disponible.

Esta PoC puede crear recursos que generen consumo. Elimina los recursos cuando termines.

---

## 2. Azure CLI

Comprueba:

```powershell
az version
```

Si no está instalado, instala Azure CLI siguiendo la documentación oficial de Microsoft.

---

## 3. Login

```powershell
az login
```

Después:

```powershell
az account show --output table
```

Si tienes varias suscripciones:

```powershell
az account list --output table
```

Selecciona una:

```powershell
az account set --subscription "NOMBRE_O_ID"
```

---

## 4. Extensión Container Apps

La PoC puede prepararla automáticamente:

```powershell
.\scripts\check-prerequisites.ps1
```

---

## 5. Imagen requerida

Azure Container Apps necesita descargar una imagen.

Configura:

```powershell
$env:CONTAINER_IMAGE="registry/imagen:tag"
```

Ejemplo conceptual:

```text
docker.io/usuario/mycomplex-poc-017:1.0.0
```

La elección definitiva del registry no forma parte de esta PoC.

---

## 6. Variables recomendadas

```powershell
$env:AZURE_RESOURCE_GROUP="rg-mycomplex-poc"
$env:AZURE_LOCATION="eastus"
$env:AZURE_CONTAINERAPPS_ENV="mycomplex-poc-env"
$env:AZURE_CONTAINERAPP_NAME="mycomplex-poc-017"
```

---

## 7. Despliegue

```powershell
.\scripts\deploy.ps1
```

---

## 8. Evidencia

Cuando termine, guarda:

```text
FQDN
estado de aprovisionamiento
revisión activa
respuesta /health
respuesta /api/info
```

---

## 9. Limpieza

```powershell
.\scripts\delete-resources.ps1
```

Esto evita mantener recursos de prueba innecesariamente.
