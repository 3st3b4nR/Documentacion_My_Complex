# Evidencias sugeridas — PoC ADR-007

## Evidencia 1

JWT de Administrador con:

```text
tenant_id = conjunto-001
roles = Administrador
```

Oculta el token completo.

## Evidencia 2

```text
GET /api/admin
→ 200 OK
```

con token de Administrador.

## Evidencia 3

```text
GET /api/admin
→ 403 Forbidden
```

con token de Residente.

## Evidencia 4

```text
GET /api/tenants/conjunto-001/datos
→ 200 OK
```

con usuario de `conjunto-001`.

## Evidencia 5

```text
GET /api/tenants/conjunto-002/datos
→ 403 Forbidden
```

con usuario de `conjunto-001`.

## Conclusión sugerida

> La PoC confirmó que el backend puede aplicar autorización utilizando los roles y el TenantId incluidos en el JWT emitido por Auth0. Un usuario autenticado no obtiene acceso únicamente por poseer un token válido: también debe cumplir el rol requerido y pertenecer al conjunto residencial solicitado. Esto demuestra la viabilidad de mantener el aislamiento lógico entre tenants y aplicar control de acceso desde el backend.
