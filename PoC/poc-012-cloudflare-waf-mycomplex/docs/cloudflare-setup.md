# Configuración de Cloudflare — PoC ADR-012

## 1. Requisito

Necesitas un dominio o subdominio que puedas administrar desde Cloudflare.

Cloudflare WAF trabaja sobre una zona/dominio agregado a la cuenta.

## 2. Agregar dominio

En Cloudflare:

```text
Add a domain / Add site
```

Completa el proceso indicado por Cloudflare para que el dominio utilice sus nameservers.

## 3. Crear un DNS record hacia el origen

Crea un registro para el subdominio de prueba.

Ejemplo:

```text
poc.mycomplex.com
```

Debe apuntar al origen público donde está ejecutándose la PoC.

Asegúrate de que el proxy de Cloudflare esté habilitado.

## 4. Verificar tráfico normal

Abre:

```text
https://poc.mycomplex.com/public
```

Debe responder el backend.

En la respuesta revisa:

```text
cfRay
```

Si tiene valor, es una evidencia útil de que la solicitud pasó por Cloudflare.

## 5. Crear Custom Rule

En el dashboard actual de Cloudflare:

```text
Security
→ Security rules
→ Create rule
→ Custom rules
```

Nombre:

```text
PoC MyComplex - bloquear ruta de prueba
```

Expresión:

```text
http.request.uri.path eq "/poc-waf-block"
```

Acción:

```text
Block
```

Selecciona:

```text
Deploy
```

Cloudflare indica que las custom rules permiten filtrar solicitudes mediante propiedades HTTP y aplicar acciones como Block.

## 6. Probar

Ruta permitida:

```text
https://poc.mycomplex.com/public
```

Debe seguir funcionando.

Ruta bloqueada:

```text
https://poc.mycomplex.com/poc-waf-block
```

Debe ser rechazada por Cloudflare.

## 7. Desactivar después de la PoC

La ruta y la regla existen exclusivamente como evidencia académica.

Después de tomar las evidencias puedes desactivar o eliminar la regla de prueba.

## Nota

El plan Free de Cloudflare incluye el Free Managed Ruleset. Para esta PoC utilizamos además una Custom Rule sencilla porque permite demostrar visualmente el funcionamiento del WAF sin ejecutar ataques reales.
