# Configuración de Resend — PoC ADR-010

## 1. Crear cuenta

Ingresa al panel de Resend y crea una cuenta.

## 2. Crear API Key

En el panel:

```text
API Keys
→ Create API Key
```

Guarda la clave en un lugar seguro.

Ejemplo de formato:

```text
re_xxxxxxxxxxxxxxxxx
```

No la subas al repositorio.

## 3. Primera prueba sin dominio propio

Para desarrollo puedes usar:

```text
MyComplex <onboarding@resend.dev>
```

La cuenta de Resend puede limitar los destinatarios cuando usas este remitente de prueba.

Usa para esta PoC el correo autorizado por tu cuenta.

## 4. Configurar variables

PowerShell:

```powershell
$env:RESEND_API_KEY="re_jJ187EfJ_M2Zw5hporzkg5i11FFadTF6k"
$env:RESEND_FROM="MyComplex <lucuaracortes@gmail.com>"
```

## 5. Ejecutar backend

```powershell
mvn spring-boot:run
```

## 6. Probar envío

```powershell
$body = @{
  to = "TU_CORREO@example.com"
  subject = "Prueba MyComplex"
  message = "Correo de prueba desde Spring Boot."
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8083/api/emails/send" `
  -ContentType "application/json" `
  -Body $body
```

## 7. Para producción

Antes de enviar desde una dirección propia de MyComplex deberás verificar un dominio en Resend y configurar correctamente sus registros DNS.

Eso no es necesario para demostrar el objetivo básico de esta PoC.
