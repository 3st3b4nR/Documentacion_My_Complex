# PoC ADR-012 — Cloudflare WAF para MyComplex

## Objetivo

Esta prueba de concepto valida la decisión del ADR-012 de utilizar **Cloudflare WAF** delante de MyComplex.

La PoC busca demostrar algo muy concreto:

```text
Solicitud legítima
      ↓
Cloudflare WAF
      ↓
Permitida
      ↓
Backend MyComplex
```

y:

```text
Solicitud que coincide con regla WAF
      ↓
Cloudflare WAF
      ↓
BLOQUEADA
      X
Backend MyComplex
```

Cloudflare documenta que su WAF inspecciona solicitudes web y API y filtra tráfico mediante rulesets. En el plan Free está disponible el **Cloudflare Free Managed Ruleset**, y las custom rules permiten aplicar acciones como `Block` a solicitudes que coincidan con una expresión.

---

## Qué demuestra

La PoC valida que:

1. El dominio de MyComplex puede quedar detrás del proxy de Cloudflare.
2. Una solicitud normal puede llegar correctamente al backend.
3. Una regla personalizada puede bloquear una ruta específica.
4. La solicitud bloqueada no alcanza al backend.
5. Cloudflare devuelve la respuesta de bloqueo antes que la aplicación.
6. El backend puede observar cabeceras como `CF-Ray` cuando la solicitud sí pasa por Cloudflare.

---

## Qué NO demuestra

Esta PoC no valida:

- Auth0;
- autorización;
- Firebase;
- correos;
- Docker;
- balanceo;
- base de datos;
- Azure;
- reglas OWASP completas;
- mitigación de ataques reales.

El objetivo es únicamente comprobar que Cloudflare WAF puede actuar como capa perimetral delante de MyComplex.

---

## Estructura

```text
poc-012-cloudflare-waf-mycomplex/
├── docs/
│   ├── cloudflare-setup.md
│   └── evidence-checklist.md
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocwaf/
│       │   ├── PocWafApplication.java
│       │   └── controller/
│       │       └── WafTestController.java
│       └── resources/
│           └── application.properties
├── .gitignore
├── pom.xml
└── README.md
```

---

## Endpoints

### Estado

```text
GET /health
```

Esperado:

```text
200 OK
```

### Tráfico permitido

```text
GET /public
```

Después de pasar el dominio por Cloudflare debe seguir devolviendo:

```text
200 OK
```

### Ruta objetivo de bloqueo

```text
GET /poc-waf-block
```

ANTES de crear la regla WAF:

```text
200 OK
```

DESPUÉS de crear la regla WAF:

```text
403 / respuesta de bloqueo de Cloudflare
```

y el backend no debe procesar esa solicitud.

---

## Ejecutar localmente

```powershell
mvn spring-boot:run
```

La aplicación se ejecuta en:

```text
http://localhost:8084
```

Puedes comprobar:

```powershell
Invoke-RestMethod http://localhost:8084/public
Invoke-RestMethod http://localhost:8084/poc-waf-block
```

Ambas rutas deben responder porque localmente Cloudflare todavía no está delante del backend.

---

## Importante: Cloudflare necesita un dominio

Para validar realmente el WAF, la aplicación debe estar accesible mediante un dominio o subdominio agregado a Cloudflare.

Ejemplo conceptual:

```text
poc.mycomplex.com
        ↓
Cloudflare
        ↓
origen donde está ejecutándose esta PoC
```

El origen puede ser cualquier entorno temporal accesible desde Internet. La decisión de dónde alojarlo no forma parte de esta PoC.

---

## Regla WAF propuesta

En Cloudflare crea una Custom Rule con la expresión:

```text
http.request.uri.path eq "/poc-waf-block"
```

Acción:

```text
Block
```

Cloudflare documenta que una custom rule se compone de una expresión de coincidencia y una acción, como `Block`.

Después despliega la regla.

---

## Prueba final

### Prueba permitida

```text
https://TU-DOMINIO/public
```

Esperado:

```text
200 OK
```

La respuesta puede incluir:

```json
{
  "result": "TRAFICO_PERMITIDO",
  "cfRay": "..."
}
```

### Prueba bloqueada

```text
https://TU-DOMINIO/poc-waf-block
```

Esperado:

```text
Cloudflare bloquea la petición
```

No debe aparecer el JSON:

```text
ORIGEN_ALCANZADO
```

Si aparece, la regla no está actuando correctamente.

---

## Criterio de éxito

```text
✓ el dominio pasa por Cloudflare;
✓ /public continúa disponible;
✓ /poc-waf-block funciona antes de la regla;
✓ /poc-waf-block queda bloqueado después de desplegar la regla;
✓ el bloqueo ocurre antes de llegar al backend;
✓ una solicitud permitida muestra evidencia de haber pasado por Cloudflare.
```

Si se cumplen estos puntos, la PoC demuestra que Cloudflare WAF es técnicamente viable como capa de protección perimetral para MyComplex.
