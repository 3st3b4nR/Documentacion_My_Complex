# Configuración exacta de Auth0 — Tenant mycomplex-dev

## Datos confirmados por la captura

```text
Tenant Name: mycomplex-dev
Region: US-5
Environment: Development
```

## 1. Confirmar el Domain real

Ve a:

```text
Auth0 Dashboard
→ Settings
→ Tenant Settings
```

Busca `Domain`.

El backend debe usar exactamente:

```text
https://<DOMAIN>/
```

como `AUTH0_ISSUER`.

No confundas `Tenant Name` con `Domain`.

## 2. Crear API

Ve a:

```text
Applications
→ APIs
→ Create API
```

Configura:

```text
Name: MyComplex API
Identifier: https://api.mycomplex.com
Signing Algorithm: RS256
```

## 3. Crear usuario

Ve a:

```text
User Management
→ Users
→ Create User
```

Ejemplo:

```text
admin@mycomplex.test
```

No uses una contraseña real.

## 4. Agregar metadata

En `app_metadata`:

```json
{
  "tenant_id": "conjunto-001"
}
```

## 5. Crear rol

```text
User Management
→ Roles
→ Create Role
```

Nombre:

```text
Administrador
```

Asigna ese rol al usuario.

## 6. Crear Action

```text
Actions
→ Library
→ Build Custom
```

Nombre:

```text
Add MyComplex Claims
```

Trigger:

```text
Login / Post Login
```

Copia:

```text
auth0/action-add-mycomplex-claims.js
```

Haz `Deploy`.

## 7. Añadirla al Login Flow

```text
Actions
→ Flows
→ Login
```

Agrega `Add MyComplex Claims` y guarda.

## 8. Configurar backend

PowerShell:

```powershell
$env:AUTH0_ISSUER="https://DOMAIN-EXACTO/"
$env:AUTH0_AUDIENCE="https://api.mycomplex.com"
mvn spring-boot:run
```
