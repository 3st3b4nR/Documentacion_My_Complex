# PoC ADR-010 — Correos transaccionales con Resend

## Objetivo

Esta prueba de concepto valida la decisión del ADR-010 de utilizar **Resend** como proveedor externo de correos transaccionales para MyComplex.

El flujo probado es:

```text
MyComplex Backend
      ↓
API HTTPS de Resend
      ↓
Resend acepta el correo
      ↓
Proveedor de correo
      ↓
Destinatario
```

El ADR de correos transaccionales buscaba evitar mantener un servidor SMTP propio y disponer de un proveedor sencillo para comunicaciones del sistema. La opción seleccionada fue Resend. fileciteturn25file1

---

## Qué demuestra

La PoC valida que:

1. Spring Boot puede comunicarse con la API de Resend.
2. La clave de API puede mantenerse fuera del código.
3. El backend puede enviar destinatario, asunto y contenido dinámico.
4. Resend devuelve un identificador del correo aceptado.
5. Un error del proveedor se maneja de forma controlada.
6. El correo puede llegar a una cuenta real durante la prueba.

---

## Estructura

```text
poc-010-resend-email-mycomplex/
├── docs/
│   ├── resend-setup.md
│   └── evidence-checklist.md
├── examples/
│   └── send-email.json
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocemail/
│       │   ├── PocEmailApplication.java
│       │   ├── config/
│       │   │   ├── ResendConfig.java
│       │   │   └── ResendProperties.java
│       │   ├── controller/
│       │   │   └── EmailController.java
│       │   ├── dto/
│       │   │   ├── EmailRequest.java
│       │   │   └── EmailResponse.java
│       │   └── service/
│       │       └── ResendEmailService.java
│       └── resources/
│           └── application.properties
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Importante

La API Key de Resend es un secreto.

Nunca debes:

```text
- escribirla directamente en Java;
- subirla a GitHub;
- incluirla en capturas;
- compartirla en el README.
```

La PoC la obtiene mediante:

```text
RESEND_API_KEY
```

---

## Configuración

Consulta:

```text
docs/resend-setup.md
```

Necesitarás una cuenta en Resend y una API Key.

Para la primera prueba se deja por defecto:

```text
MyComplex <onboarding@resend.dev>
```

Con el dominio de onboarding, utiliza como destinatario el correo permitido por tu cuenta de Resend. Para enviar libremente a otros destinatarios, verifica un dominio propio en Resend.

---

## Ejecutar

### PowerShell

```powershell
$env:RESEND_API_KEY="re_TU_API_KEY"
$env:RESEND_FROM="MyComplex <onboarding@resend.dev>"

mvn spring-boot:run
```

Servidor:

```text
http://localhost:8083
```

---

## Verificar que está activo

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri "http://localhost:8083/health"
```

Esperado:

```json
{
  "status": "UP",
  "service": "poc-010-resend-email"
}
```

---

## Enviar correo

```powershell
$body = @{
  to = "TU_CORREO@example.com"
  subject = "Prueba MyComplex"
  message = "Este correo valida la PoC ADR-010."
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8083/api/emails/send" `
  -ContentType "application/json" `
  -Body $body
```

Respuesta esperada:

```json
{
  "success": true,
  "providerId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "providerStatus": 200,
  "message": "Resend acepto el correo para envio"
}
```

Después verifica que el correo haya llegado.

---

## Casos de prueba

### Caso 1

```text
GET /health
→ 200 OK
```

### Caso 2

Solicitud válida:

```text
POST /api/emails/send
→ Resend acepta
→ 200 OK
```

### Caso 3

API Key ausente:

```text
RESEND_API_KEY vacía
→ error controlado
```

### Caso 4

API Key inválida:

```text
Resend rechaza la petición
→ backend responde error controlado
```

### Caso 5

Correo real recibido:

```text
Resend
→ destinatario
→ correo visible
```

---

## Qué NO valida

Esta PoC no prueba:

- Auth0;
- autorización;
- Firebase;
- PostgreSQL;
- colas;
- historial de correos;
- reintentos automáticos;
- Docker;
- Azure;
- CI/CD.

La única decisión evaluada es la viabilidad técnica de Resend como proveedor de correo transaccional.

---

## Criterio de éxito

```text
✓ Spring Boot consume la API de Resend.
✓ La API Key permanece fuera del código.
✓ Resend acepta un correo válido.
✓ Se recibe un identificador del mensaje.
✓ El correo llega al destinatario de prueba.
✓ Los errores del proveedor se controlan.
```

Si estos puntos se cumplen, la PoC confirma que Resend es técnicamente viable para los correos transaccionales de MyComplex.
