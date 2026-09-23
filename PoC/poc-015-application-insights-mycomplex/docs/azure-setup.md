# Configuración de Application Insights — PoC ADR-015

## 1. Crear Application Insights

En Azure Portal busca:

```text
Application Insights
→ Create
```

Crea un recurso para desarrollo.

Nombre sugerido:

```text
mycomplex-poc-monitoring
```

---

## 2. Obtener Connection String

Dentro del recurso busca:

```text
Overview
→ Connection String
```

Cópialo.

No lo subas a GitHub.

En PowerShell:

```powershell
$env:APPLICATIONINSIGHTS_CONNECTION_STRING="TU_CONNECTION_STRING"
```

---

## 3. Descargar Java Agent

Descarga el Application Insights Java Agent desde la documentación/repositorio oficial de Microsoft.

Guárdalo en la raíz de esta PoC como:

```text
applicationinsights-agent.jar
```

El archivo está excluido mediante `.gitignore`.

---

## 4. Ejecutar

```powershell
.\scripts\run-with-insights.ps1
```

El script:

```text
1. compila con Maven;
2. localiza el JAR generado;
3. inicia Java con -javaagent;
4. utiliza APPLICATIONINSIGHTS_CONNECTION_STRING.
```

---

## 5. Generar tráfico

En otra terminal:

```powershell
.\scripts\generate-traffic.ps1
```

---

## 6. Esperar ingestión

La telemetría puede tardar algunos minutos en aparecer.

Luego revisa:

```text
Application Insights
→ Investigate
→ Application map / Performance / Failures

o

Monitoring
→ Logs
```

---

## 7. Consultar requests

```kusto
requests
| order by timestamp desc
| take 20
```

---

## 8. Consultar errores

```kusto
exceptions
| order by timestamp desc
| take 20
```

---

## Seguridad

No incluyas en logs:

```text
contraseñas
JWT completos
tokens
API keys
datos financieros
datos personales innecesarios
```

La PoC genera únicamente información artificial.
