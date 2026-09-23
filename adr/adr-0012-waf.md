# proteger la aplicacion contra ataques web

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex estará disponible en Internet y podrá recibir solicitudes legítimas y también intentos de ataque. Se necesita una protección que revise el tráfico antes de que llegue a la aplicación y que no obligue al equipo a mantener un sistema de seguridad complejo por su cuenta.

## Impulsores de decisión

* Proteger la aplicación frente a ataques web comunes.
* Evitar que solicitudes claramente maliciosas lleguen directamente al backend.
* Mantener una solución fácil de administrar para un equipo pequeño.
* Contar con registros que ayuden a identificar intentos de ataque.

## Opciones consideradas

* Azure Front Door Premium con Web Application Firewall (WAF)
* Cloudflare WAF
* WAF administrado por el equipo con Nginx y ModSecurity

## Resultado de la decisión

Opción elegida: **"Azure Front Door Premium con Web Application Firewall (WAF)"**, porque permite revisar y bloquear tráfico peligroso antes de que llegue a MyComplex, incluye reglas de protección ya preparadas y se puede administrar desde la misma plataforma donde se alojarán otros servicios del sistema. Esto reduce el trabajo de mantenimiento y mejora la seguridad sin exigir que el equipo construya su propia solución.

### Consecuencias positivas

* Se bloquean muchas solicitudes peligrosas antes de que lleguen a la aplicación.
* Se pueden agregar reglas de protección sin modificar el código de MyComplex.
* Se facilita el seguimiento de solicitudes bloqueadas y posibles ataques.
* Se integra con otros servicios de Azure utilizados por el proyecto.

### Consecuencias negativas

* Genera un costo adicional de infraestructura.
* Será necesario revisar las reglas para evitar que se bloquee tráfico legítimo por error.
* Aumenta la dependencia de los servicios de Azure.

## Pros y contras de las opciones

### Azure Front Door Premium con Web Application Firewall (WAF)

Servicio administrado que protege aplicaciones publicadas en Internet.

* Bien, porque incluye protecciones listas para usar y permite crear reglas propias.
* Bien, porque el equipo no tiene que instalar ni mantener servidores adicionales para esta función.
* Malo, porque tiene un costo recurrente.
* Malo, porque aumenta la dependencia de Azure.

### Cloudflare WAF

Servicio externo especializado en proteger aplicaciones y sitios web.

* Bien, porque ofrece una protección amplia y una configuración sencilla.
* Bien, porque puede funcionar aunque la aplicación esté alojada en otro proveedor.
* Malo, porque agrega otro proveedor que el equipo debe administrar.
* Malo, porque algunas funciones avanzadas dependen del plan contratado.

### WAF administrado por el equipo con Nginx y ModSecurity

Protección instalada y mantenida directamente por el equipo de MyComplex.

* Bien, porque ofrece mayor control sobre la configuración.
* Bien, porque no depende de un servicio de WAF administrado por terceros.
* Malo, porque el equipo debe encargarse de actualizaciones, reglas y disponibilidad.
* Malo, porque añade trabajo técnico que no aporta directamente a las funciones principales del sistema.

## Enlaces

* Relacionado con el ADR 07 de autorización y control de acceso.
* Relacionado con el ADR 24 de publicación segura mediante dominio y HTTPS.
* Relacionado con el ADR 15 de monitoreo de la plataforma.
