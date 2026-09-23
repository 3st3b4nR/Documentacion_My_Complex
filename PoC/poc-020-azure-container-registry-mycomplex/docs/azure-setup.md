# Configuración Azure — PoC ADR-020

## 1. Requisitos

Debes tener:

```text
Azure CLI
Docker
suscripción de Azure activa
```

---

## 2. Iniciar sesión

```powershell
az login
```

Verifica:

```powershell
az account show --output table
```

---

## 3. Crear ACR

```powershell
.\scripts\create-acr.ps1
```

El SKU utilizado en esta PoC es:

```text
Basic
```

porque es suficiente para demostrar almacenamiento y versionado de imágenes.

---

## 4. Nombre único

Los nombres de ACR son globalmente únicos.

Ejemplo válido:

```text
mycomplexacr5831
```

Solo utiliza:

```text
letras
números
```

---

## 5. Configurar variables

```powershell
$env:AZURE_ACR_NAME="mycomplexacr5831"
$env:ACR_REPOSITORY="mycomplex-backend"
$env:IMAGE_TAG="1.0.0"
```

---

## 6. Publicar imagen

```powershell
.\scripts\build-tag-push.ps1
```

---

## 7. Consultar

```powershell
.\scripts\show-images.ps1
```

---

## 8. Descargar y ejecutar

```powershell
.\scripts\pull-and-run.ps1
```

Después:

```text
http://localhost:8089/health
```

---

## 9. Eliminar recursos

Cuando termines:

```powershell
.\scripts\delete-acr.ps1
```

ACR puede generar costo mientras exista, por lo que conviene eliminar el recurso de prueba después de obtener las evidencias.
