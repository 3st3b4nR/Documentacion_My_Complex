# Evidencias sugeridas — PoC ADR-009

## Evidencia 1 — Proyecto Firebase

Captura de Firebase Console mostrando el proyecto.

No mostrar secretos ni cuentas de servicio.

## Evidencia 2 — Token FCM

Captura de la página de la PoC mostrando que se generó el token.

Puedes ocultar parte del token.

## Evidencia 3 — Solicitud al backend

```text
POST /api/notifications/send
→ 200 OK
```

Debe observarse un `messageId`.

## Evidencia 4 — Notificación recibida

Captura de la notificación visible en Windows/navegador.

## Evidencia 5 — Token inválido

```text
POST /api/notifications/send
token inválido
→ error controlado
```

## Conclusión sugerida

> La PoC confirmó que Firebase Cloud Messaging permite registrar un navegador mediante un token FCM y que el backend Spring Boot puede enviar notificaciones push utilizando Firebase Admin SDK. Firebase aceptó el mensaje y lo entregó al navegador, demostrando la viabilidad técnica de FCM como canal de notificaciones para MyComplex.
