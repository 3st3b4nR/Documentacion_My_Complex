# Evidencias sugeridas — PoC ADR-012

## Evidencia 1 — Backend sin regla

Captura de:

```text
GET /poc-waf-block
→ 200 OK
```

antes de activar la regla.

Esto demuestra que el endpoint existe realmente en el origen.

## Evidencia 2 — Regla Cloudflare

Captura del dashboard mostrando:

```text
Rule:
PoC MyComplex - bloquear ruta de prueba

Expression:
http.request.uri.path eq "/poc-waf-block"

Action:
Block
```

## Evidencia 3 — Tráfico legítimo

```text
GET /public
→ 200 OK
```

después de activar la regla.

Así demostramos que la regla no bloqueó todo el sistema.

## Evidencia 4 — Tráfico bloqueado

```text
GET /poc-waf-block
→ bloqueado por Cloudflare
```

Captura de la respuesta/página de bloqueo.

## Evidencia 5 — Cloudflare en la ruta

Captura de `/public` mostrando un valor en:

```text
cfRay
```

## Conclusión sugerida

> La PoC confirmó que Cloudflare puede ubicarse delante del origen de MyComplex y aplicar reglas WAF antes de que una solicitud llegue al backend. El tráfico permitido continuó llegando normalmente al sistema, mientras una ruta definida como bloqueada fue rechazada en la capa perimetral, demostrando la viabilidad técnica de Cloudflare WAF para el ADR-012.
