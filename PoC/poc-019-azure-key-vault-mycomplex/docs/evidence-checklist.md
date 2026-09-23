# Evidencias sugeridas — PoC ADR-019

## Evidencia 1 — Key Vault

Captura de Azure mostrando el recurso Key Vault.

No mostrar claves ni valores de secretos.

## Evidencia 2 — Código sin secreto

Mostrar:

```text
application.properties
```

con:

```text
azure.keyvault.url=${AZURE_KEY_VAULT_URL:}
```

y sin credenciales hardcodeadas.

## Evidencia 3 — Crear secreto

```text
POST /api/secrets
→ 200 OK
```

La respuesta debe mostrar:

```text
name
version
```

sin revelar el valor.

## Evidencia 4 — Azure Portal

En:

```text
Key Vault
→ Objects
→ Secrets
```

mostrar que existe:

```text
resend-api-key-poc
```

No abrir ni enseñar el valor.

## Evidencia 5 — Metadata

```text
GET /api/secrets/resend-api-key-poc/metadata
```

Debe mostrar:

```text
version
createdOn
updatedOn
```

## Evidencia 6 — Lectura comprobada

Ejecutar:

```text
POST /api/secrets/resend-api-key-poc/verify
```

y demostrar:

```text
success = true
```

sin que el backend devuelva el secreto.

## Evidencia 7 — Rotación/versionado

Guardar otro valor utilizando el mismo nombre.

Comprobar que:

```text
version anterior != version nueva
```

## Conclusión sugerida

> La PoC confirmó que MyComplex puede almacenar y recuperar credenciales mediante Azure Key Vault sin mantener sus valores directamente en el código fuente. El secreto quedó centralizado, pudo modificarse generando una nueva versión y el backend logró consultarlo sin exponer su valor en las respuestas. Esto demuestra la viabilidad técnica de Azure Key Vault para la decisión del ADR-019.
