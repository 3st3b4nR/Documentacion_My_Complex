# Evidencias sugeridas — PoC ADR-015

## Evidencia 1 — Recurso Application Insights

Captura del recurso:

```text
mycomplex-poc-monitoring
```

No mostrar el Connection String completo.

## Evidencia 2 — Backend activo

```text
GET /health
→ 200 OK
```

## Evidencia 3 — Requests

Captura de Application Insights mostrando solicitudes de:

```text
/health
/api/random
/api/work
```

## Evidencia 4 — Rendimiento

Mostrar una solicitud:

```text
/api/work?delayMs=1500
```

con duración aproximada de 1.5 segundos.

## Evidencia 5 — Error

```text
GET /api/error
→ 500
```

y luego captura de `Failures` o consulta en Logs.

## Evidencia 6 — Consulta KQL

Ejecutar:

```kusto
requests
| order by timestamp desc
| take 20
```

## Conclusión sugerida

> La PoC confirmó que Azure Monitor Application Insights puede centralizar la telemetría del backend de MyComplex y permitir la consulta de solicitudes, tiempos de respuesta, errores y logs sin desplegar una plataforma de monitoreo propia. Esto demuestra la viabilidad técnica de la decisión tomada en el ADR-015.
