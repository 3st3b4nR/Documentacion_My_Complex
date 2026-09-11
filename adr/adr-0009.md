# implementar el servicio de notificaciones push

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-11

## Contexto y planteamiento del problema

MyComplex requiere enviar alertas en tiempo real a los usuarios (ej. notificar a un residente la llegada de un visitante a portería). Dado que el sistema operará principalmente a través de navegadores web y una PWA (Progressive Web App) para los vigilantes, es necesario definir la infraestructura que enviará estos mensajes. Desarrollar y mantener un motor propio de WebSockets o notificaciones persistentes añade una carga operativa y de infraestructura que el equipo no puede asumir actualmente. ¿Qué servicio externo de mensajería Push es el más adecuado para el proyecto?

## Impulsores de decisión

* Garantizar la entrega de notificaciones en tiempo real a dispositivos web y móviles.
* Mantener los costos de infraestructura en cero durante las fases de desarrollo y validación.
* Contar con una integración oficial, nativa y estable con el backend en Java (Spring Boot).
* Delegar la carga de mantener conexiones persistentes a un servicio administrado experto.

## Opciones consideradas

* Desarrollo propio con WebSockets.
* Firebase Cloud Messaging (FCM).
* OneSignal.

## Resultado de la decisión

Opción elegida: **Firebase Cloud Messaging (FCM)**, porque es el estándar de la industria respaldado por Google, ofrece un servicio de nivel empresarial sin costo (100% gratuito sin límite de envíos) y cuenta con un SDK oficial sumamente maduro (`firebase-admin`) para integrarse directamente en el backend de Java. 

El backend se encargará exclusivamente de disparar el evento de notificación a la API de Firebase, delegando en este último la responsabilidad de despertar el *Service Worker* en el navegador del usuario y entregar el mensaje.

### Consecuencias positivas

* Cero costos operativos recurrentes por el envío de notificaciones.
* Alta fiabilidad y escalabilidad garantizada por la infraestructura de Google.
* Integración limpia y tipada en el backend gracias al SDK oficial de Java.

### Consecuencias negativas

* Requiere una configuración técnica manual de certificados y *Service Workers* en el código del frontend.
* Añade una dependencia directa hacia los servicios de Google Cloud.

## Pros y contras de las opciones

### Desarrollo propio con WebSockets
* Bien, porque ofrece control absoluto sobre el canal de comunicación.
* Malo, porque escalar conexiones persistentes consume muchos recursos del servidor de alojamiento y aumenta los costos.
* Malo, porque no funciona de manera confiable cuando la aplicación web está en segundo plano o cerrada en dispositivos móviles.

### Firebase Cloud Messaging (FCM)
* Bien, porque es gratuito, altamente escalable y estándar en el mercado.
* Bien, porque tiene soporte nativo robusto para entornos Java.
* Malo, porque su configuración inicial en el cliente web tiene una curva de aprendizaje técnica.

### OneSignal
* Bien, porque abstrae gran parte de la complejidad del frontend con una configuración visual muy sencilla.
* Malo, porque su capa gratuita tiene límites que, al superarse, imponen costos que afectan el presupuesto del proyecto a escala.