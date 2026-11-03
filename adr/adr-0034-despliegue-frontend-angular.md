# desplegar el frontend angular en Azure Static Web Apps

* Estado de decisión: Aceptado
* Estado de implementación: Documentado como arquitectura objetivo
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-11-03

Historia técnica: seleccionar la plataforma de publicación para la SPA Angular definida en ADR-0011.

## Contexto y planteamiento del problema

MyComplex utiliza una aplicación Angular y TypeScript que, después del proceso de construcción, produce archivos estáticos HTML, CSS y JavaScript. El frontend debe publicarse mediante HTTPS, consumir exclusivamente la URL pública del API Gateway e integrarse con Auth0 y Firebase Cloud Messaging sin exponer secretos en el navegador.

La plataforma de publicación debe soportar rutas internas de la SPA, dominio personalizado, distribución y caché de contenido, integración con GitHub Actions, despliegues reproducibles y recuperación de una versión conocida. ¿Dónde debe desplegarse el frontend Angular con una complejidad y un costo operativo adecuados para MyComplex?

No se encontró evidencia local de un despliegue actual del frontend. Por esta razón, la decisión queda aceptada como arquitectura objetivo y no se presenta como implementada.

## Impulsores de decisión

* Publicar de forma eficiente los recursos estáticos generados por Angular.
* Proporcionar HTTPS administrado y permitir un dominio personalizado.
* Distribuir y almacenar en caché contenido estático sin operar un servidor dedicado.
* Integrar el despliegue con GitHub Actions y conservar trazabilidad de cada ejecución.
* Mantener costo y complejidad operacional acordes con el proyecto.
* Resolver las rutas de Angular mediante fallback a `index.html`.
* Configurar por ambiente la URL pública del API Gateway.
* Registrar correctamente en Auth0 el origen y las URL públicas del frontend.
* Publicar `firebase-messaging-sw.js` en la ubicación y con el alcance requeridos.
* Restringir CORS en el Gateway al origen exacto del frontend.
* Permitir recuperar o volver a desplegar una versión anterior conocida.
* Configurar encabezados de seguridad y evitar secretos dentro del bundle.

## Opciones consideradas

* Azure Static Web Apps.
* Azure Storage Static Website con Azure Front Door o CDN.
* Contenedor Nginx desplegado en Azure Container Apps.
* Azure App Service.

## Resultado de la decisión

Opción elegida: **"Azure Static Web Apps"**, porque Angular genera contenido estático y no requiere mantener un servidor Nginx o un proceso de aplicación únicamente para entregar archivos. La plataforma proporciona publicación administrada mediante HTTPS, distribución de contenido e integración con repositorios GitHub, y permite configurar el fallback requerido por una SPA.

Esta decisión mantiene el frontend separado de Spring Cloud Gateway y del backend Spring Boot. El navegador consume el Gateway mediante su URL pública. No se conecta directamente a servicios internos ni traslada al frontend validaciones de autorización que corresponden al Gateway y al backend.

### Angular y rutas

Las rutas internas administradas por Angular deben devolver `index.html` cuando la solicitud no corresponda a un archivo estático real. La configuración de rutas no debe interceptar assets, `firebase-messaging-sw.js` ni otros archivos que deban servirse directamente.

### Auth0

La URL pública definitiva debe registrarse correctamente en **Allowed Callback URLs**, **Allowed Logout URLs** y **Allowed Web Origins**. El client ID, dominio y audience son configuración pública del cliente SPA. Ningún client secret, token administrativo o credencial de servidor puede incluirse en el frontend.

### Firebase Cloud Messaging

`firebase-messaging-sw.js` debe publicarse en la ubicación que conserve el scope requerido por la aplicación. La clave pública VAPID y la configuración pública de Firebase deben diferenciarse de las credenciales administrativas. El JSON de cuenta de servicio de Firebase pertenece al backend y nunca debe publicarse en el repositorio o bundle del frontend.

### Gateway y CORS

El frontend debe consumir la URL pública de Spring Cloud Gateway. El Gateway debe aceptar el origen exacto de Azure Static Web Apps mediante CORS y continuar validando JWT y permisos. Las rutas protegidas deben verificarse en el Gateway y el backend, no solamente mediante guards de Angular.

### Configuración por ambiente

Las URL del Gateway, Auth0 y Firebase y los demás valores públicos pueden inyectarse durante el build o cargarse desde una configuración pública de ejecución. Cada ambiente debe producir una configuración trazable. Una SPA no puede proteger secretos incluidos en JavaScript, aunque los valores hayan sido suministrados por variables del workflow.

### Caché, Service Worker y recuperación

Los assets deben versionarse para facilitar su invalidación. Después de cada despliegue se debe comprobar la actualización del Service Worker, evitar que una caché antigua mezcle versiones incompatibles y verificar que las notificaciones continúen funcionando. La recuperación se realizará conservando un artefacto conocido o reconstruible y volviendo a desplegar esa versión mediante el proceso automatizado.

### Seguridad

La publicación utilizará HTTPS. Se configurarán CSP y otros encabezados de seguridad cuando la plataforma lo permita, sin incluir secretos en el bundle. CORS aceptará únicamente los orígenes necesarios y la autorización real permanecerá en Gateway y backend. ADR-0012 selecciona Cloudflare WAF como arquitectura objetivo, pero este ADR no afirma que actualmente proteja al frontend desplegado.

### Consecuencias positivas

* No se requiere operar Nginx ni otra aplicación solo para servir archivos estáticos.
* HTTPS, distribución y publicación quedan administrados por la plataforma.
* El despliegue puede integrarse con GitHub Actions y relacionarse con cada cambio.
* El frontend permanece desacoplado del ciclo de despliegue del Gateway y del backend.
* El fallback permite usar navegación directa y recarga en rutas internas de Angular.
* La opción es coherente con la infraestructura Azure adoptada por MyComplex.

### Consecuencias negativas

* La solución agrega dependencia de Azure Static Web Apps y de su configuración de rutas y encabezados.
* Auth0, Firebase, CORS y las URL públicas deben mantenerse sincronizados entre ambientes.
* Una política de caché incorrecta puede conservar bundles o Service Workers antiguos.
* Los valores públicos quedan visibles en el JavaScript descargado por el navegador.
* Las capacidades que excedan la entrega de una SPA estática deben residir en Gateway, backend u otros servicios.
* La recuperación depende de conservar o poder reconstruir el artefacto correspondiente a una versión conocida.

## Pros y contras de las opciones

### Azure Static Web Apps

Servicio administrado orientado a aplicaciones web cuyos recursos se construyen desde un repositorio.

* Bien, porque se ajusta al resultado estático del build de Angular.
* Bien, porque proporciona HTTPS y distribución administrada sin operar un servidor web.
* Bien, porque facilita la integración con GitHub y despliegues trazables.
* Bien, porque permite configurar el fallback de las rutas SPA.
* Bien, porque reduce la carga operativa y mantiene separado el frontend.
* Malo, porque requiere adaptar rutas, encabezados y configuración a las capacidades del servicio.
* Malo, porque introduce dependencia de un servicio específico de Azure.

### Azure Storage Static Website con Azure Front Door o CDN

Publicación de archivos desde Storage complementada con una capa de distribución y entrada pública.

* Bien, porque utiliza almacenamiento simple y escalable para recursos estáticos.
* Bien, porque Front Door o CDN pueden aportar distribución, dominio y caché.
* Bien, porque ofrece control separado sobre almacenamiento y entrega.
* Malo, porque exige configurar y operar más recursos para obtener el conjunto completo de capacidades.
* Malo, porque aumenta la complejidad de rutas SPA, encabezados, dominio y automatización.
* Malo, porque resulta menos directa para el alcance actual que Static Web Apps.

### Contenedor Nginx en Azure Container Apps

Imagen de contenedor que incorpora el build de Angular y utiliza Nginx para servirlo.

* Bien, porque ofrece control detallado sobre rutas, caché y encabezados HTTP.
* Bien, porque reutiliza Container Apps, Docker y Azure Container Registry.
* Bien, porque puede ejecutarse de forma semejante entre ambientes.
* Malo, porque requiere construir, publicar, actualizar y supervisar una imagen adicional.
* Malo, porque consume recursos de ejecución para servir archivos estáticos.
* Malo, porque agrega un servidor que no aporta lógica de negocio al frontend.

### Azure App Service

Plataforma administrada para alojar aplicaciones y servidores web.

* Bien, porque ofrece HTTPS, dominio personalizado y operación administrada.
* Bien, porque permite configuraciones de despliegue maduras dentro de Azure.
* Malo, porque sus capacidades de ejecución son mayores que las requeridas por una SPA estática.
* Malo, porque implica más configuración y costo relativo que una opción dedicada a contenido estático.
* Malo, porque no ofrece una ventaja suficiente para servir únicamente el build de Angular.

## Enlaces

* Stack de la SPA: [ADR-0011: Angular y TypeScript](adr-0011-stack-frontend.md).
* Autenticación y configuración de Auth0: [ADR-0006](adr-0006-autenticacion.md).
* Notificaciones y Service Worker de Firebase: [ADR-0009](adr-0009-notificaciones-push.md).
* Protección perimetral documentada como arquitectura objetivo: [ADR-0012: Cloudflare WAF](adr-0012-waf.md).
* Empaquetado considerado por la alternativa Nginx: [ADR-0016: Docker](adr-0016-empaquetado-docker.md).
* Plataforma de nube: [ADR-0017: Azure Container Apps](adr-0017-ejecucion-nube.md).
* Automatización: [ADR-0022](adr-0022-automatizacion-pruebas-despliegues.md) y [ADR-0028](adr-0028-cicd.md).
* Punto público de acceso a las API: [ADR-0032: Spring Cloud Gateway](adr-0032-API-Gateway.md).
* Observabilidad de la solución: [ADR-0033](adr-0033-arquitectura-observabilidad.md).
