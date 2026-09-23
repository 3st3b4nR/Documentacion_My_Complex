# Evidencias sugeridas — PoC ADR-010

## Evidencia 1 — API Key creada

Captura del panel de Resend mostrando que existe una API Key.

No muestres el valor completo.

## Evidencia 2 — Backend activo

```text
GET /health
→ 200 OK
```

## Evidencia 3 — Envío aceptado

```text
POST /api/emails/send
→ 200 OK
```

La respuesta debe mostrar un `providerId`.

## Evidencia 4 — Panel de Resend

Captura del correo registrado como enviado/aceptado en Resend.

## Evidencia 5 — Correo recibido

Captura del correo en la bandeja del destinatario.

## Evidencia 6 — Error controlado

Prueba con una API Key inválida o sin configurar.

El backend debe responder el error sin exponer la clave.

## Conclusión sugerida

> La PoC confirmó que el backend Spring Boot de MyComplex puede consumir la API HTTPS de Resend y enviar correos transaccionales sin mantener un servidor SMTP propio. La credencial del proveedor se conserva fuera del código y Resend devuelve un identificador para cada solicitud aceptada, demostrando la viabilidad técnica de la decisión del ADR-010.
