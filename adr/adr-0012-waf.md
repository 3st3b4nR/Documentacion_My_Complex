# proteger la aplicación con OWASP Core Rule Set en un contenedor

* Estado de decisión: Aceptado
* Estado de implementación: Implementado
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-11-04

Historia técnica: actualizar la decisión para reflejar el WAF implementado por el equipo mediante OWASP Core Rule Set en un contenedor.

## Contexto y planteamiento del problema

MyComplex está expuesto a solicitudes HTTP provenientes de Internet y necesita filtrar tráfico malicioso antes de que llegue a los componentes protegidos. La solución debe detectar patrones comunes de ataque, generar registros para investigar eventos y ejecutarse de forma coherente con el uso de contenedores adoptado por el proyecto.

El equipo implementó OWASP Core Rule Set en un contenedor. Este ADR documenta esa solución y evita confundir el conjunto de reglas con el motor WAF que las ejecuta. ¿Cómo debe protegerse el tráfico web de MyComplex con una solución controlada por el equipo y desplegable como contenedor?

## Impulsores de decisión

* Detectar y bloquear patrones comunes de ataque contra aplicaciones web y API.
* Filtrar las solicitudes antes de encaminarlas hacia el componente protegido.
* Utilizar un conjunto de reglas abierto, mantenido y ampliamente revisado.
* Ejecutar el WAF como contenedor para mantener un despliegue repetible.
* Integrarse con la estrategia Docker de MyComplex.
* Permitir ajustar reglas y exclusiones cuando existan falsos positivos.
* Generar logs de seguridad que puedan investigarse junto con la observabilidad del sistema.
* Evitar depender obligatoriamente de un WAF SaaS externo.
* Mantener control sobre versiones, configuración y actualización de reglas.

## Opciones consideradas

* OWASP Core Rule Set en un contenedor con un motor WAF compatible con ModSecurity.
* Cloudflare WAF administrado.
* Azure Front Door Premium con Web Application Firewall.
* WAF instalado directamente en una máquina o servidor administrado por el equipo.
* Publicar la aplicación sin una capa WAF.

## Resultado de la decisión

Opción elegida: **"OWASP Core Rule Set en un contenedor con un motor WAF compatible con ModSecurity"**, porque permite aplicar reglas abiertas contra ataques web comunes, mantener la configuración bajo control del equipo y desplegar la protección de forma repetible mediante contenedores.

**OWASP Core Rule Set (CRS) es el conjunto de reglas de detección.** Las reglas necesitan un motor WAF compatible, como ModSecurity, para inspeccionar las solicitudes y tomar la acción configurada. El contenedor reúne el motor, CRS y el componente de proxy necesario para recibir tráfico y reenviar únicamente las solicitudes permitidas.

Este repositorio documental no contiene la configuración operativa del contenedor y no permite verificar si la variante utilizada se basa en Apache o Nginx. Por ello, el ADR no fija una variante, etiqueta de imagen, nivel de paranoia, umbral de anomalía, puerto o ruta de despliegue que no estén documentados localmente.

El WAF protege exclusivamente el tráfico que se encamina a través de su contenedor. Los componentes no expuestos detrás de esta ruta no quedan protegidos automáticamente. La topología de publicación debe evitar rutas alternativas que permitan acceder directamente al servicio protegido y omitir la inspección.

La configuración debe mantener una versión controlada de CRS, revisar actualizaciones de la imagen, registrar los eventos de seguridad y ajustar exclusiones específicas cuando una solicitud legítima active una regla. Deshabilitar reglas de manera general reduce la cobertura y debe evitarse cuando sea posible resolver el caso con una exclusión acotada.

### Consecuencias positivas

* MyComplex dispone de un WAF implementado con reglas abiertas de OWASP CRS.
* El contenedor facilita repetir el despliegue entre ambientes.
* El equipo controla versiones, reglas, umbrales y exclusiones.
* La solución puede proteger componentes existentes sin incorporar lógica WAF dentro del código de la aplicación.
* Los logs del motor permiten investigar solicitudes bloqueadas y reglas activadas.
* La arquitectura evita depender obligatoriamente de Cloudflare o de una edición Premium de Azure Front Door.

### Consecuencias negativas

* El equipo debe actualizar, proteger, supervisar y respaldar la configuración del contenedor.
* Una configuración demasiado estricta puede bloquear solicitudes legítimas.
* Una configuración demasiado permisiva puede reducir la protección esperada.
* El análisis de solicitudes consume CPU, memoria y añade latencia.
* El contenedor se convierte en un componente que debe mantenerse disponible.
* Las reglas genéricas requieren ajustes para los endpoints y formatos reales de MyComplex.
* Una ruta pública que omita el WAF permitiría acceder al servicio sin inspección.
* El WAF reduce exposición a ciertos ataques, pero no corrige vulnerabilidades ni reemplaza autenticación, autorización o validaciones del backend.

## Pros y contras de las opciones

### OWASP Core Rule Set en un contenedor

CRS se ejecuta mediante un motor WAF compatible dentro de una imagen desplegable como proxy inverso.

* Bien, porque utiliza reglas abiertas orientadas a ataques web comunes.
* Bien, porque permite controlar versiones y configuración.
* Bien, porque encaja con Docker y el despliegue mediante contenedores.
* Bien, porque admite ajustes y exclusiones específicos para MyComplex.
* Bien, porque evita introducir lógica de filtrado dentro del Gateway o backend.
* Malo, porque exige operación, actualización y monitoreo por parte del equipo.
* Malo, porque requiere pruebas para controlar falsos positivos y falsos negativos.
* Malo, porque añade consumo de recursos, latencia y un componente adicional en la ruta.

### Cloudflare WAF administrado

Servicio perimetral que filtra tráfico antes de llegar a la infraestructura de MyComplex.

* Bien, porque reduce la infraestructura WAF que debe operar el equipo.
* Bien, porque integra protección perimetral y mitigación de tráfico malicioso.
* Malo, porque traslada configuración y operación a un proveedor externo.
* Malo, porque las funciones disponibles dependen del plan contratado.
* Malo, porque no corresponde a la implementación realizada por el equipo.

### Azure Front Door Premium con Web Application Firewall

Servicio administrado de entrada global y WAF dentro de Azure.

* Bien, porque se integra con recursos y control de acceso del ecosistema Azure.
* Bien, porque reduce la administración del motor y las reglas base.
* Malo, porque agrega costo y configuración superiores a la solución contenedorizada adoptada.
* Malo, porque no corresponde al WAF implementado actualmente.

### WAF instalado directamente en un servidor

Motor y reglas instalados en una máquina administrada por el equipo.

* Bien, porque permite un control semejante al contenedor.
* Malo, porque dificulta repetir y versionar el despliegue.
* Malo, porque aumenta la dependencia de la configuración particular del servidor.
* Malo, porque se aparta de la estrategia de empaquetado con Docker.

### Publicar sin WAF

Las solicitudes llegarían directamente al Gateway o al backend.

* Bien, porque elimina un componente y su latencia.
* Malo, porque pierde una capa de detección y bloqueo de ataques web comunes.
* Malo, porque deja toda la mitigación en manos de la aplicación y otros controles.

## Enlaces

* Autenticación: [ADR-0006](adr-0006-autenticacion.md).
* Autorización: [ADR-0007](adr-0007-autorizacion.md).
* Empaquetado con Docker: [ADR-0016](adr-0016-empaquetado-docker.md).
* Ejecución de contenedores en Azure: [ADR-0017](adr-0017-ejecucion-nube.md).
* Registro de imágenes: [ADR-0020](adr-0020-registro-contenedores.md).
* Acceso público centralizado: [ADR-0032](adr-0032-API-Gateway.md).
* Observabilidad y consulta de logs: [ADR-0033](adr-0033-arquitectura-observabilidad.md).
* Proyecto oficial: [OWASP Core Rule Set](https://coreruleset.org/).
* Imagen oficial: [ModSecurity Docker con OWASP CRS](https://github.com/coreruleset/modsecurity-crs-docker).
