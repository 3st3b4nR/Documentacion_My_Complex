# PoC ADR-019 — Azure Key Vault

## Objetivo

Esta prueba de concepto valida la decisión del ADR-019 de utilizar **Azure Key Vault** para proteger contraseñas, claves y credenciales de MyComplex.

El ADR establece que secretos como credenciales de PostgreSQL, claves de Resend, Firebase y otros recursos no deben quedar escritos directamente en el código ni almacenados en el repositorio. La opción elegida fue Azure Key Vault. fileciteturn29file2

---

## Qué queremos demostrar

La PoC valida este flujo:

```text
Aplicación Spring Boot
        ↓
Azure Key Vault
        ↓
secreto centralizado
```

y demuestra que el backend puede:

```text
guardar
consultar metadata
leer
verificar
actualizar/versionar
```

un secreto sin escribir su valor dentro del código fuente.

---

## Separación de responsabilidades

Esta PoC corresponde únicamente a:

```text
ADR-019
¿Dónde guardamos los secretos?
```

No decide todavía:

```text
ADR-024 → cómo se identifica un servicio ante Azure.
ADR-023 → cómo se protege el acceso de red.
```

Durante desarrollo local utilizamos `DefaultAzureCredential`, que puede aprovechar la sesión iniciada con Azure CLI.

Eso es únicamente un mecanismo para ejecutar la prueba.

La decisión sobre **Managed Identities** se valida en su propia PoC.

---

## Estructura

```text
poc-019-azure-key-vault-mycomplex/
├── docs/
│   ├── azure-setup.md
│   └── evidence-checklist.md
├── examples/
│   ├── create-secret.json
│   └── verify-secret.json
├── scripts/
│   ├── create-key-vault.ps1
│   ├── show-account.ps1
│   └── test-poc.ps1
├── src/
│   └── main/
│       ├── java/com/mycomplex/pockeyvault/
│       │   ├── PocKeyVaultApplication.java
│       │   ├── config/
│       │   │   ├── KeyVaultConfig.java
│       │   │   └── KeyVaultProperties.java
│       │   ├── controller/
│       │   │   └── KeyVaultController.java
│       │   ├── dto/
│       │   │   ├── SecretMetadataResponse.java
│       │   │   ├── SecretResponse.java
│       │   │   └── SecretWriteRequest.java
│       │   └── service/
│       │       └── KeyVaultService.java
│       └── resources/
│           └── application.properties
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Seguridad de la PoC

No uses credenciales reales.

Para probar utiliza algo como:

```text
nombre:
resend-api-key-poc

valor:
valor-falso-solo-para-la-poc
```

El endpoint de metadata **no devuelve el valor secreto**.

Esto permite demostrar que el secreto existe sin mostrarlo.

---

## Requisitos

Necesitas:

```text
Azure CLI
cuenta/suscripción Azure
Azure Key Vault
Java 21
Maven
```

---

## Paso 1 — Login

```powershell
az login
```

Comprueba:

```powershell
az account show
```

---

## Paso 2 — Crear Key Vault

Puedes usar:

```powershell
.\scripts\create-key-vault.ps1
```

El script crea un Key Vault con un nombre único y muestra una URL similar a:

```text
https://kv-mycomplex-poc-1234.vault.azure.net/
```

---

## Paso 3 — Permisos

Tu identidad local debe tener permiso para manejar secretos en el Key Vault.

Consulta:

```text
docs/azure-setup.md
```

La configuración exacta depende de los permisos disponibles en tu suscripción.

---

## Paso 4 — Configurar URL

```powershell
$env:AZURE_KEY_VAULT_URL="https://TU-KEY-VAULT.vault.azure.net/"
```

---

## Paso 5 — Ejecutar

```powershell
mvn spring-boot:run
```

La API usa:

```text
http://localhost:8088
```

---

## Paso 6 — Probar automáticamente

```powershell
.\scripts\test-poc.ps1
```

La prueba:

```text
1. comprueba /health
2. guarda un secreto
3. consulta metadata
4. vuelve a leerlo desde Key Vault
5. comprueba que coincide
```

---

## Guardar un secreto

```powershell
$body = @{
  name = "resend-api-key-poc"
  value = "valor-falso-solo-para-la-poc"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8088/api/secrets" `
  -ContentType "application/json" `
  -Body $body
```

Respuesta aproximada:

```json
{
  "success": true,
  "name": "resend-api-key-poc",
  "version": "...",
  "message": "Secreto almacenado correctamente en Azure Key Vault"
}
```

El valor secreto no aparece en la respuesta.

---

## Consultar metadata

```powershell
Invoke-RestMethod `
  "http://localhost:8088/api/secrets/resend-api-key-poc/metadata"
```

Debe mostrar:

```text
name
version
enabled
createdOn
updatedOn
```

pero no:

```text
value
```

---

## Verificar lectura

```powershell
$body = @{
  expectedValue = "valor-falso-solo-para-la-poc"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8088/api/secrets/resend-api-key-poc/verify" `
  -ContentType "application/json" `
  -Body $body
```

Esperado:

```json
{
  "success": true,
  "name": "resend-api-key-poc",
  "version": "...",
  "message": "El valor leído desde Key Vault coincide con el esperado"
}
```

Así comprobamos que Spring Boot realmente recuperó el valor desde Key Vault sin mostrarlo en la respuesta.

---

## Probar una nueva versión

Vuelve a llamar:

```text
POST /api/secrets
```

con el mismo nombre pero un valor nuevo:

```text
valor-falso-version-2
```

Azure Key Vault genera una nueva versión del secreto.

Luego consulta:

```text
/api/secrets/resend-api-key-poc/metadata
```

y verifica que el identificador de versión cambió.

Esto ayuda a demostrar que una credencial puede cambiarse sin modificar el código fuente, uno de los objetivos del ADR. fileciteturn29file2

---

## Qué NO valida

Esta PoC no prueba:

```text
- Managed Identity;
- Azure RBAC definitivo para producción;
- Private Endpoint;
- VNet;
- PostgreSQL;
- Resend real;
- Firebase real;
- Container Apps;
- CI/CD.
```

Cada aspecto corresponde a otra PoC.

---

## Criterios de éxito

La PoC se considera exitosa si:

```text
✓ el secreto se almacena fuera del código;
✓ Spring Boot puede guardar un secreto en Key Vault;
✓ Spring Boot puede recuperar el secreto;
✓ la API no devuelve el valor secreto en metadata;
✓ el secreto tiene una versión identificable;
✓ cambiar el valor genera una nueva versión;
✓ el código fuente no contiene la credencial;
✓ no se necesita modificar ni recompilar la aplicación para cambiar el secreto.
```

Si estos puntos se cumplen, se demuestra la viabilidad técnica de Azure Key Vault para la decisión del ADR-019.
