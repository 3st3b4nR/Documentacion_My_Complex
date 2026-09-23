# Preparación del entorno — PoC ADR-016

## 1. Requisitos

Necesitas:

```text
Docker Desktop
```

o una instalación compatible de Docker Engine.

Comprueba:

```powershell
docker --version
```

y:

```powershell
docker info
```

---

## 2. No necesitas Maven local para construir con Docker

El Dockerfile utiliza una imagen de Maven en la etapa de construcción.

Por eso puedes ejecutar:

```powershell
docker build -t mycomplex/poc-016-docker:1.0.0 .
```

aunque Maven no esté instalado directamente en Windows.

Sí necesitas conexión a Internet la primera vez para descargar las imágenes base y dependencias.

---

## 3. Construir

```powershell
docker build -t mycomplex/poc-016-docker:1.0.0 .
```

---

## 4. Ejecutar

```powershell
docker run --name mycomplex-poc-016 `
  -p 8087:8080 `
  -e APP_ENVIRONMENT=docker `
  mycomplex/poc-016-docker:1.0.0
```

---

## 5. Probar

En otra terminal:

```powershell
Invoke-RestMethod http://localhost:8087/health
Invoke-RestMethod http://localhost:8087/api/info
```

---

## 6. Ver contenedores

```powershell
docker ps
```

Debes observar:

```text
mycomplex-poc-016
```

---

## 7. Detener

```powershell
docker stop mycomplex-poc-016
```

---

## 8. Eliminar contenedor

```powershell
docker rm mycomplex-poc-016
```

La imagen permanece disponible.

---

## 9. Ejecutar otra vez

```powershell
docker run --name mycomplex-poc-016 `
  -p 8087:8080 `
  mycomplex/poc-016-docker:1.0.0
```

Esto demuestra que la misma imagen puede reutilizarse.
