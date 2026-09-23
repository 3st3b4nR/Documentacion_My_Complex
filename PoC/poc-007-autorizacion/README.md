# PoC ADR-007 — Autorización y aislamiento por TenantId

## Objetivo

Esta PoC valida la decisión del ADR-007 de MyComplex: el backend debe aplicar la autorización usando la información contenida en el JWT emitido por Auth0.

La PoC demuestra dos reglas principales:

1. **Autorización por rol**
   - un Administrador puede acceder a endpoints administrativos;
   - un Residente no puede acceder a endpoints exclusivos de Administrador.

2. **Aislamiento por TenantId**
   - un usuario solo puede acceder a recursos del conjunto residencial al que pertenece;
   - un usuario de `conjunto-001` no debe poder consultar recursos de `conjunto-002`.

## Dependencia de la PoC-006

Esta PoC asume que Auth0 ya emite un Access Token con los claims:

```text
https://mycomplex.com/tenant_id
https://mycomplex.com/roles
```

La autenticación ya fue validada en la PoC-006. Aquí nos concentramos únicamente en autorización.

## Flujo

```text
JWT válido
   ↓
Spring Security
   ↓
extrae roles y TenantId
   ↓
¿tiene el rol requerido?
   ↓
¿pertenece al tenant solicitado?
   ↓
200 / 403
```

## Endpoints

### Público

```text
GET /public
```

No requiere token.

### Usuario autenticado

```text
GET /api/me
```

Devuelve subject, TenantId, roles y authorities.

### Solo Administrador

```text
GET /api/admin
```

Requiere:

```text
ROLE_ADMINISTRADOR
```

### Residente o Administrador

```text
GET /api/residente
```

Permite:

```text
ROLE_RESIDENTE
ROLE_ADMINISTRADOR
```

### Acceso por TenantId

```text
GET /api/tenants/{tenantId}/datos
```

Solo permite acceso cuando:

```text
tenantId de la URL == tenantId del JWT
```

### Administrador dentro de su tenant

```text
GET /api/tenants/{tenantId}/admin
```

Exige simultáneamente:

```text
rol = Administrador
y
tenantId solicitado = tenantId del JWT
```

## Configuración

Usa las mismas variables de entorno de Auth0 de la PoC-006:

```powershell
$env:AUTH0_ISSUER="https://TU-DOMINIO-AUTH0/"
$env:AUTH0_AUDIENCE="https://api.mycomplex.com"
```

La aplicación usa por defecto el puerto:

```text
8081
```

## Ejecución

```powershell
mvn spring-boot:run
```

## Casos de prueba

Supongamos un JWT con:

```json
{
  "https://mycomplex.com/tenant_id": "conjunto-001",
  "https://mycomplex.com/roles": ["Administrador"]
}
```

### 1. Administrador accede a endpoint administrativo

```bash
curl http://localhost:8081/api/admin   -H "Authorization: Bearer TU_TOKEN"
```

Esperado:

```text
200 OK
```

### 2. Residente intenta acceder a endpoint administrativo

Con un JWT cuyo rol sea:

```text
Residente
```

Esperado:

```text
403 Forbidden
```

### 3. Usuario accede a su propio tenant

```bash
curl http://localhost:8081/api/tenants/conjunto-001/datos   -H "Authorization: Bearer TU_TOKEN"
```

Esperado:

```text
200 OK
```

### 4. Usuario intenta acceder a otro tenant

```bash
curl http://localhost:8081/api/tenants/conjunto-002/datos   -H "Authorization: Bearer TU_TOKEN"
```

Esperado:

```text
403 Forbidden
```

### 5. Administrador intenta administrar otro tenant

```bash
curl http://localhost:8081/api/tenants/conjunto-002/admin   -H "Authorization: Bearer TU_TOKEN"
```

Esperado:

```text
403 Forbidden
```

## Qué NO valida esta PoC

Esta PoC no prueba:

- persistencia real en PostgreSQL;
- filtrado automático de consultas SQL;
- WAF;
- Docker;
- CI/CD;
- secretos;
- otros servicios externos.

El propósito es exclusivamente demostrar que la política de autorización por rol y TenantId es viable en el backend.

## Criterio de éxito

La PoC se considera exitosa si:

```text
✓ un Administrador puede acceder a /api/admin;
✓ un Residente recibe 403 en /api/admin;
✓ un usuario puede acceder a su propio tenant;
✓ un usuario recibe 403 al intentar acceder a otro tenant;
✓ el backend obtiene roles y TenantId directamente del JWT;
✓ no se utilizan credenciales o permisos definidos por el frontend.
```
