# PoC ADR-009 — Notificaciones Push con Firebase Cloud Messaging

## Objetivo

Esta prueba de concepto valida la decisión del ADR-009 de utilizar **Firebase Cloud Messaging (FCM)** para las notificaciones push de MyComplex.

El objetivo es demostrar este flujo:

```text
Navegador / PWA
      ↓
obtiene token FCM
      ↓
Backend Spring Boot
      ↓
Firebase Cloud Messaging
      ↓
Navegador / dispositivo
      ↓
notificacion visible
```

La PoC está limitada exclusivamente a demostrar el canal de notificación push.

No implementa el historial completo de notificaciones, reglas de negocio, autenticación, base de datos ni preferencias del usuario.

---

## Qué demuestra

La PoC valida que:

1. Un navegador puede solicitar permiso para notificaciones.
2. Firebase puede generar un token FCM para ese navegador.
3. El backend Java/Spring Boot puede utilizar Firebase Admin SDK.
4. El backend puede enviar una notificación hacia Firebase.
5. Firebase puede entregar la notificación al navegador.
6. La notificación puede visualizarse con la aplicación abierta o en segundo plano.

Esto corresponde al comportamiento esperado de MyComplex para eventos como visitas, comunicados o reuniones. El documento funcional del proyecto contempla que el sistema envíe notificaciones push y que el dispositivo las muestre al usuario. 

---

## Estructura

```text
poc-009-firebase-push-mycomplex/
├── examples/
│   └── send-notification.json
├── docs/
│   ├── firebase-setup.md
│   └── evidence-checklist.md
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocpush/
│       │   ├── PocPushApplication.java
│       │   ├── config/FirebaseConfig.java
│       │   ├── controller/PushController.java
│       │   ├── dto/
│       │   │   ├── PushRequest.java
│       │   │   └── PushResponse.java
│       │   └── service/PushNotificationService.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── index.html
│               ├── firebase-messaging-sw.js
│               └── js/
│                   ├── app.js
│                   └── firebase-config.js
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Configuración necesaria

Consulta:

```text
docs/firebase-setup.md
```

Necesitarás:

- proyecto en Firebase;
- aplicación Web registrada;
- configuración pública Firebase del cliente;
- clave pública VAPID;
- cuenta de servicio JSON para el backend.

**Nunca subas el archivo JSON de la cuenta de servicio al repositorio.**

---

## Ejecutar backend

Configura la variable:

### PowerShell

```powershell
$env:FIREBASE_CREDENTIALS="C:\\ruta\\firebase-service-account.json"
mvn spring-boot:run
```

La PoC usa:

```text
http://localhost:8082
```

---

## Obtener token FCM

Abre:

```text
http://localhost:8082
```

Pulsa:

```text
Habilitar notificaciones y obtener token
```

Acepta el permiso del navegador.

La página mostrará un token FCM.

---

## Enviar una notificación

Con PowerShell:

```powershell
$body = @{
  token = "TOKEN_FCM"
  title = "MyComplex"
  body = "Tienes una nueva notificacion de prueba"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8082/api/notifications/send" `
  -ContentType "application/json" `
  -Body $body
```

Respuesta esperada:

```json
{
  "success": true,
  "messageId": "projects/.../messages/...",
  "message": "Notificacion enviada a Firebase Cloud Messaging"
}
```

Después debe aparecer una notificación en el navegador.

---

## Casos de prueba

### Caso 1 — Backend activo

```text
GET /health
→ 200 OK
```

### Caso 2 — Obtener token FCM

```text
Abrir /
→ permitir notificaciones
→ obtener token
```

### Caso 3 — Envío válido

```text
POST /api/notifications/send
token válido
→ 200 OK
```

### Caso 4 — Recepción

```text
Firebase
→ navegador
→ notificación visible
```

### Caso 5 — Token inválido

Usar un token incorrecto.

Resultado esperado:

```text
El backend recibe un error de Firebase
y responde 502.
```

---

## Qué NO demuestra

Esta PoC no intenta validar:

- Auth0;
- autorización;
- base de datos;
- historial de notificaciones;
- preferencias del usuario;
- reintentos complejos;
- correos;
- Docker;
- Azure;
- CI/CD.

Todo eso se evalúa en otras decisiones.

---

## Criterio de éxito

```text
✓ Firebase genera un token FCM.
✓ Spring Boot se autentica ante Firebase mediante una cuenta de servicio.
✓ El backend envía una notificación a FCM.
✓ Firebase devuelve un messageId.
✓ El navegador recibe la notificación.
✓ Un token inválido produce un error controlado.
```

Si se cumplen estos puntos, se demuestra que Firebase Cloud Messaging es técnicamente viable como canal de notificaciones push para MyComplex.
