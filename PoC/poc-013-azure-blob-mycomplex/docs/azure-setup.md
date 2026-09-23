# Configuración de Azure Blob Storage — PoC ADR-013

## 1. Crear Storage Account

En Azure Portal busca:

```text
Storage accounts
→ Create
```

Para esta PoC utiliza una cuenta de desarrollo.

## 2. Crear contenedor

Dentro de la Storage Account:

```text
Data storage
→ Containers
→ + Container
```

Nombre recomendado:

```text
mycomplex-noticias
```

Mantén el acceso público deshabilitado.

## 3. Obtener Connection String

Para una PoC local:

```text
Security + networking
→ Access keys
```

Obtén el `Connection string`.

No lo publiques ni lo guardes en GitHub.

## 4. Configurar variable de entorno

PowerShell:

```powershell
$env:AZURE_STORAGE_CONNECTION_STRING="DefaultEndpointsProtocol=..."
$env:AZURE_STORAGE_CONTAINER="mycomplex-noticias"
```

## 5. Ejecutar

```powershell
mvn spring-boot:run
```

## 6. Verificar

```text
http://localhost:8085/health
```

## 7. Seguridad

Esta PoC usa Connection String únicamente para demostrar la integración desde desarrollo local.

La decisión sobre identidades administradas y acceso entre servicios corresponde a otros ADR y no debe mezclarse con esta prueba.

En el sistema real se debe evitar exponer claves de Storage Account cuando exista un mecanismo de identidad administrada disponible.
