# Configuración de Azure Key Vault — PoC ADR-019

## 1. Iniciar sesión

```powershell
az login
```

Verifica:

```powershell
az account show --output table
```

---

## 2. Crear Key Vault

Ejecuta:

```powershell
.\scripts\create-key-vault.ps1
```

También puedes crearlo manualmente desde Azure Portal.

Nombre sugerido:

```text
kv-mycomplex-poc-XXXX
```

---

## 3. URL del Key Vault

La URL tiene esta forma:

```text
https://NOMBRE.vault.azure.net/
```

Configura:

```powershell
$env:AZURE_KEY_VAULT_URL="https://NOMBRE.vault.azure.net/"
```

---

## 4. Dar permiso a tu usuario

Para ejecutar esta PoC local necesitas que el usuario con el que hiciste:

```text
az login
```

pueda trabajar con secretos.

Si el Key Vault usa Azure RBAC, una opción para la PoC es asignar al usuario un rol de secretos apropiado desde:

```text
Key Vault
→ Access control (IAM)
→ Add role assignment
```

Debes utilizar únicamente el nivel de permiso necesario para la prueba.

---

## 5. Cómo se autentica Java

El código utiliza:

```java
new DefaultAzureCredentialBuilder().build()
```

En desarrollo local puede utilizar la identidad disponible mediante Azure CLI.

Por eso:

```powershell
az login
```

debe realizarse antes de ejecutar la aplicación.

En Azure, `DefaultAzureCredential` también puede trabajar con otros mecanismos de identidad, pero esa decisión se prueba por separado en el ADR correspondiente.

---

## 6. Ejecutar

```powershell
mvn spring-boot:run
```

---

## 7. Probar

```powershell
.\scripts\test-poc.ps1
```

---

## 8. No usar secretos reales

Utiliza exclusivamente valores ficticios.

Ejemplo:

```text
valor-falso-solo-para-la-poc
```

No uses:

```text
contraseñas reales
API keys reales
tokens JWT
datos financieros
credenciales de producción
```
