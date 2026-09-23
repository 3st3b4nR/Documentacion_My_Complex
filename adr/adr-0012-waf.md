# proteger la aplicacion contra ataques web

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

MyComplex estará disponible en Internet y recibirá solicitudes legítimas, pero también podrá recibir tráfico automatizado y ataques dirigidos a la aplicación web y a su API. Se necesita seleccionar una solución de protección web que filtre solicitudes peligrosas antes de que lleguen al sistema, que sea sencilla de operar para un equipo pequeño y que pueda utilizarse inicialmente sin generar un costo fijo elevado.

## Impulsores de decisión

* Proteger la aplicación frente a ataques web comunes (Seguridad).
* Evitar que solicitudes claramente maliciosas lleguen directamente al backend (Seguridad).
* Mantener una solución fácil de administrar para un equipo pequeño (Operabilidad).
* Disponer de registros y controles básicos que ayuden a identificar tráfico sospechoso (Operabilidad y Cumplimiento).
* Evitar una plataforma que genere un costo fijo alto desde la etapa académica y de validación (Eficiencia de costo).
* Permitir que la solución pueda ampliarse en el futuro si MyComplex aumenta su nivel de criticidad o tráfico (Mantenibilidad y Escalabilidad).
* Reducir la necesidad de instalar, actualizar y proteger infraestructura propia para el firewall (Mantenibilidad).

## Opciones consideradas

* Cloudflare WAF en su plan gratuito, con posibilidad de escalar posteriormente a un plan de pago.
* Azure Front Door Premium con Web Application Firewall (WAF).
* WAF administrado por el equipo con Nginx y ModSecurity.

## Resultado de la decisión

Opción elegida: **"Cloudflare WAF en su plan gratuito, con posibilidad de escalar posteriormente a un plan de pago"**, porque ofrece una protección inicial suficiente para la etapa actual de MyComplex sin exigir un costo mensual fijo desde el comienzo.

El plan gratuito de Cloudflare incluye Web Application Firewall, el **Cloudflare Free Managed Ruleset**, protección DDoS no medida, CDN y certificado SSL universal. Además, permite crear un número limitado de reglas personalizadas para bloquear o desafiar solicitudes que cumplan condiciones definidas por el equipo.

Esta alternativa permite comenzar con una cobertura de seguridad razonable para el entorno académico y de validación, manteniendo el costo inicial en cero para las funciones incluidas en el plan gratuito. Si MyComplex evoluciona hacia una operación más crítica, con mayor tráfico, necesidades avanzadas de reglas o requisitos empresariales, se podrá migrar a un plan superior sin cambiar el concepto arquitectónico de utilizar un WAF administrado delante de la aplicación.

La decisión prioriza simplicidad, costo y protección suficiente para la etapa actual, evitando adoptar desde el inicio una solución Premium cuya capacidad y costo serían desproporcionados frente al tamaño del proyecto.

### Consecuencias positivas

* MyComplex obtiene protección web administrada sin un costo mensual fijo inicial por el plan gratuito.
* Se bloquean ataques y vulnerabilidades de alto impacto mediante reglas administradas incluidas en el plan gratuito.
* Se reduce la exposición directa de la aplicación frente a tráfico malicioso.
* Cloudflare incluye protección DDoS no medida y certificado SSL universal dentro del plan gratuito.
* El equipo no debe instalar ni mantener un servidor WAF propio.
* Se pueden agregar reglas personalizadas para necesidades específicas de MyComplex.
* La solución puede escalar posteriormente a un plan superior si las necesidades de seguridad aumentan.
* Reduce la carga operativa y de mantenimiento del equipo.

### Consecuencias negativas

* El plan gratuito dispone de un conjunto más limitado de reglas que los planes superiores.
* El Cloudflare Managed Ruleset completo y el OWASP Core Ruleset no están incluidos en el plan gratuito.
* El número de reglas personalizadas disponibles en el plan gratuito es limitado.
* Algunas funciones avanzadas de seguridad, análisis y soporte requieren planes de pago.
* MyComplex dependerá de Cloudflare para la protección perimetral.
* Será necesario revisar periódicamente que la cobertura gratuita siga siendo suficiente para el nivel de riesgo de la plataforma.

## Pros y contras de las opciones

### Cloudflare WAF en su plan gratuito

Servicio de seguridad perimetral administrado que puede colocarse delante de la aplicación y la API de MyComplex.

* Bien, porque el plan gratuito incluye WAF y el Cloudflare Free Managed Ruleset.
* Bien, porque permite comenzar sin pagar una suscripción mensual fija.
* Bien, porque incluye protección DDoS no medida.
* Bien, porque incluye CDN y certificado SSL universal, reduciendo la necesidad de agregar servicios adicionales para estas funciones básicas.
* Bien, porque permite reglas personalizadas aun en el plan gratuito.
* Bien, porque no requiere instalar ni mantener infraestructura propia.
* Bien, porque puede ampliarse a planes superiores si MyComplex crece.
* Malo, porque el conjunto gratuito de reglas administradas es más limitado que el disponible en planes pagos.
* Malo, porque algunas capacidades avanzadas requieren actualizar el plan.
* Malo, porque aumenta la dependencia de un proveedor externo.

### Azure Front Door Premium con Web Application Firewall (WAF)

Servicio administrado de Azure con WAF integrado y capacidades avanzadas de seguridad y distribución.

* Bien, porque ofrece reglas administradas avanzadas e integración con otros servicios Azure.
* Bien, porque permite centralizar seguridad, distribución y publicación dentro del ecosistema Azure.
* Bien, porque es adecuado para aplicaciones empresariales con requisitos elevados.
* Malo, porque introduce un costo fijo considerable desde el inicio.
* Malo, porque su capacidad resulta sobredimensionada para la etapa académica y de validación de MyComplex.
* Malo, porque aumenta la dependencia del ecosistema Azure.

### WAF administrado por el equipo con Nginx y ModSecurity

Firewall desplegado, configurado y mantenido directamente por el equipo de MyComplex.

* Bien, porque ofrece alto control sobre las reglas y la configuración.
* Bien, porque evita depender de un servicio WAF administrado.
* Malo, porque requiere instalar, actualizar, proteger y monitorear el servicio.
* Malo, porque introduce un nuevo componente que puede convertirse en punto de falla.
* Malo, porque aumenta la carga operativa y de mantenimiento del equipo.
* Malo, porque traslada al equipo responsabilidades de seguridad que un servicio administrado puede resolver con menor esfuerzo.

## Enlaces

* Relacionado con el ADR de autorización y control de acceso.
* Relacionado con el ADR de publicación mediante dominio y HTTPS.
* Relacionado con el ADR de monitoreo de la plataforma.
* Relacionado con el atributo de calidad de Seguridad.
* Cloudflare Plans: https://www.cloudflare.com/plans/
* Cloudflare WAF - Get Started: https://developers.cloudflare.com/waf/get-started/
* Cloudflare Managed Rules: https://developers.cloudflare.com/waf/managed-rules/
* Cloudflare Custom Rules: https://developers.cloudflare.com/waf/custom-rules/
