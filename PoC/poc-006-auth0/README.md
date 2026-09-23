# PoC ADR-006 — Autenticación con Auth0

Tenant configurado para esta PoC:

```text
Tenant Name: mycomplex-dev
Region: US-5
Environment: Development
```

## Importante sobre el dominio

El nombre del tenant **no sustituye necesariamente al Domain**.

Esta PoC deja como valor por defecto:

```text
https://mycomplex-dev.us.auth0.com/
```

pero antes de ejecutarla debes verificar en:

```text
Auth0 Dashboard
→ Settings
→ Domain
```

Si Auth0 muestra un dominio distinto, usa ese valor exacto en `AUTH0_ISSUER`.

## Audience esperado

En:

```text
Applications
→ APIs
→ Create API
```

crea:

```text
Name: MyComplex API
Identifier: https://api.mycomplex.com
Signing Algorithm: RS256
```

El Identifier debe coincidir con:

```text
AUTH0_AUDIENCE=https://api.mycomplex.com
```

## Variables de entorno — PowerShell

Si el Domain mostrado por Auth0 es exactamente `mycomplex-dev.us.auth0.com`:

```powershell
$env:AUTH0_ISSUER="https://mycomplex-dev.us.auth0.com/"
$env:AUTH0_AUDIENCE="https://api.mycomplex.com"
mvn spring-boot:run
```

Si tu Domain es otro, reemplaza únicamente `AUTH0_ISSUER`.

## Flujo de la PoC

```text
Usuario
  ↓
Auth0
  ↓
Access Token JWT
  ↓
Spring Boot
  ├── valida firma
  ├── valida issuer
  └── valida audience
        ↓
     /private
```

## Rutas

### Salud

```bash
curl http://localhost:8080/health
```

Esperado: `200 OK`.

### Pública

```bash
curl http://localhost:8080/public
```

Esperado: `200 OK`.

### Privada sin token

```bash
curl -i http://localhost:8080/private
```

Esperado: `401 Unauthorized`.

### Privada con Access Token

```bash
curl http://localhost:8080/private   -H "Authorization: Bearer TU_ACCESS_TOKEN"
```

Esperado:

```json
{
  "message": "Usuario autenticado correctamente",
  "subject": "auth0|...",
  "issuer": "https://mycomplex-dev.us.auth0.com/",
  "audience": ["https://api.mycomplex.com"],
  "tenantId": "conjunto-001",
  "roles": ["Administrador"]
}
```

## Claims de MyComplex

La Action incluida en:

```text
auth0/action-add-mycomplex-claims.js
```

agrega:

```text
https://mycomplex.com/tenant_id
https://mycomplex.com/roles
```

al Access Token.

## Lo que falta configurar manualmente en Auth0

El código ya está listo, pero Auth0 requiere que tú realices estas configuraciones en tu cuenta:

1. Confirmar el `Domain` exacto.
2. Crear `MyComplex API`.
3. Crear un usuario de prueba.
4. Agregar `app_metadata.tenant_id`.
5. Crear un rol `Administrador`.
6. Asignarlo al usuario.
7. Crear y desplegar la Action.
8. Añadir la Action al Login Flow.
9. Obtener un Access Token para el audience `https://api.mycomplex.com`.

No necesitas colocar un Client Secret en este backend para validar JWT.
