# PoC ADR-015 — Azure Monitor Application Insights

## Objetivo

Esta prueba de concepto valida la decisión del ADR-015 de utilizar **Azure Monitor Application Insights** como plataforma de monitoreo para MyComplex.

El objetivo no es definir todavía la estrategia definitiva de instrumentación del sistema. Esta PoC utiliza el Java Agent únicamente como mecanismo práctico para generar y enviar telemetría durante la prueba.

La decisión que se valida es:

```text
¿Application Insights permite observar el comportamiento de MyComplex
sin tener que operar una plataforma de monitoreo propia?
```

---

## Qué demuestra

La PoC permite comprobar que Application Insights puede recibir y mostrar:

```text
- solicitudes HTTP;
- tiempos de respuesta;
- códigos de estado;
- excepciones;
- logs;
- operación general del backend.
```

El flujo es:

```text
Usuario / script
      ↓
Spring Boot
      ↓
Java Agent de Application Insights
      ↓
Azure Monitor Application Insights
      ↓
Requests / Failures / Logs / Performance
```

---

## Importante

El ADR-015 selecciona la **plataforma de monitoreo**.

Esta PoC no pretende tomar una decisión arquitectónica adicional sobre OpenTelemetry, SDKs o agentes.

Para poder probar la plataforma necesitamos producir telemetría, y por eso utilizamos temporalmente el Java Agent.

---

## Estructura

```text
poc-015-application-insights-mycomplex/
├── docs/
│   ├── azure-setup.md
│   └── evidence-checklist.md
├── scripts/
│   ├── generate-traffic.ps1
│   └── run-with-insights.ps1
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocmonitoring/
│       │   ├── PocMonitoringApplication.java
│       │   └── controller/
│       │       ├── GlobalExceptionHandler.java
│       │       └── MonitoringController.java
│       └── resources/
│           └── application.properties
├── applicationinsights.json
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## Endpoints de prueba

### Salud

```text
GET /health
```

Esperado:

```text
200 OK
```

### Operación lenta controlada

```text
GET /api/work?delayMs=1000
```

Esto permite generar una solicitud cuyo tiempo de respuesta sea fácil de observar.

### Tráfico aleatorio

```text
GET /api/random
```

Genera solicitudes con diferentes tiempos.

### Error intencional

```text
GET /api/error
```

Esperado:

```text
500 Internal Server Error
```

Se utiliza únicamente para comprobar que Application Insights registra fallos y excepciones.

---

## Configuración

Consulta:

```text
docs/azure-setup.md
```

Necesitas:

```text
Application Insights
+
Connection String
+
Application Insights Java Agent
```

El Connection String se guarda como variable de entorno:

```text
APPLICATIONINSIGHTS_CONNECTION_STRING
```

No debe escribirse directamente en el repositorio.

---

## Ejecutar

Primero configura:

```powershell
$env:APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=...;IngestionEndpoint=..."
```

Coloca el Java Agent en la raíz con el nombre:

```text
applicationinsights-agent.jar
```

Luego:

```powershell
.\scripts\run-with-insights.ps1
```

La aplicación utiliza:

```text
http://localhost:8086
```

---

## Generar telemetría

En otra terminal:

```powershell
.\scripts\generate-traffic.ps1
```

El script genera:

```text
10 solicitudes normales
+
1 error intencional
```

También puedes probar manualmente:

```powershell
Invoke-RestMethod http://localhost:8086/health
Invoke-RestMethod "http://localhost:8086/api/work?delayMs=1500"
```

---

## Qué revisar en Azure

Después de generar tráfico, abre el recurso de Application Insights.

Busca información relacionada con:

```text
Requests
Failures
Performance
Logs
```

Debes poder observar las solicitudes realizadas por esta PoC.

---

## Consultas de ejemplo

En Logs puedes buscar solicitudes:

```kusto
requests
| order by timestamp desc
| take 20
```

Errores:

```kusto
exceptions
| order by timestamp desc
| take 20
```

Solicitudes lentas:

```kusto
requests
| where duration > 1s
| order by duration desc
```

Códigos 500:

```kusto
requests
| where resultCode == "500"
| order by timestamp desc
```

---

## Qué NO valida

Esta PoC no prueba:

```text
- alertas definitivas;
- dashboards de producción;
- retención definitiva de logs;
- OpenTelemetry como decisión arquitectónica;
- Docker;
- Container Apps;
- Key Vault;
- Auth0;
- base de datos;
- CI/CD.
```

Cada una corresponde a otra decisión.

---

## Criterio de éxito

La PoC se considera exitosa si:

```text
✓ Spring Boot ejecuta normalmente.
✓ Application Insights recibe telemetría.
✓ Azure muestra solicitudes HTTP.
✓ Azure muestra tiempos de respuesta.
✓ /api/error genera un fallo observable.
✓ los logs pueden consultarse centralizadamente.
✓ no es necesario operar un servidor propio de monitoreo.
```

Si se cumplen estos puntos, Application Insights demuestra ser técnicamente viable como plataforma de monitoreo para MyComplex.
