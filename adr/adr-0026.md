# definir los servicios que expone mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

El frontend de MyComplex necesita comunicarse con el backend para consultar y modificar información relacionada con usuarios, visitantes, reservas, noticias, accesos y demás funciones del sistema. Se debe definir qué tipo de servicios expondrá el backend para que esta comunicación sea clara, sencilla de mantener y adecuada para las necesidades actuales del proyecto.

## Impulsores de decisión

* Facilitar la comunicación entre la aplicación web y el backend.
* Mantener una solución sencilla de entender y desarrollar para el equipo actual.
* Permitir trabajar con recursos claramente definidos como visitantes, reservas, noticias y usuarios.
* Mantener compatibilidad directa con Java y Spring Boot.
* Evitar incorporar tecnologías más complejas si no resuelven una necesidad actual del sistema.
* Facilitar la integración con autenticación, autorización y el modelo multi-tenant.
* Mantener las notificaciones en tiempo real separadas del mecanismo principal de consulta y modificación de información.

## Opciones consideradas

* API REST sobre HTTP/HTTPS utilizando JSON.
* GraphQL.
* gRPC.
* WebSockets.
* Server-Sent Events (SSE).

## Resultado de la decisión

Opción elegida: **"API REST sobre HTTP/HTTPS utilizando JSON"**, porque MyComplex trabaja principalmente con recursos y operaciones claramente identificables, como registrar visitantes, consultar noticias, crear reservas o actualizar información de residentes. REST permite realizar estas operaciones de manera sencilla, comprensible y ampliamente compatible con Spring Boot y aplicaciones web.

REST será el mecanismo principal de comunicación entre el frontend y el backend. Las notificaciones push no utilizarán WebSockets ni SSE, ya que esa necesidad ya será atendida mediante Firebase Cloud Messaging.

### Consecuencias positivas

* Facilita el desarrollo y consumo de los servicios desde el frontend.
* Utiliza un modelo de comunicación ampliamente conocido y documentado.
* Se integra directamente con las herramientas disponibles en Spring Boot.
* Permite representar los datos mediante JSON de forma clara.
* Simplifica las pruebas de los servicios utilizando herramientas comunes.
* Evita agregar tecnologías adicionales que no son necesarias actualmente.
* Permite mantener Firebase Cloud Messaging como solución especializada para notificaciones push.

### Consecuencias negativas

* Algunas pantallas podrían requerir más de una solicitud para obtener toda la información necesaria.
* Será necesario diseñar correctamente las rutas y respuestas para evitar servicios inconsistentes.
* Si en el futuro aparecen necesidades de transmisión continua de grandes cantidades de información, podría ser necesario evaluar otros mecanismos complementarios.

## Pros y contras de las opciones

### API REST sobre HTTP/HTTPS utilizando JSON

Permite que el frontend realice solicitudes sobre recursos definidos mediante direcciones y operaciones HTTP.

* Bien, porque es sencillo de implementar y consumir.
* Bien, porque Spring Boot cuenta con soporte directo y maduro para construir APIs REST.
* Bien, porque se adapta naturalmente a recursos como usuarios, visitantes, reservas y noticias.
* Bien, porque puede protegerse fácilmente mediante los mecanismos de autenticación y autorización ya definidos.
* Malo, porque algunas consultas complejas pueden requerir varias solicitudes al backend.

### GraphQL

Permite que el frontend indique exactamente qué información desea recibir en cada consulta.

* Bien, porque puede reducir la cantidad de información innecesaria enviada al frontend.
* Bien, porque ofrece flexibilidad para construir consultas complejas.
* Malo, porque introduce una forma adicional de definir y mantener consultas y modelos.
* Malo, porque MyComplex no presenta actualmente una necesidad que justifique esa complejidad.
* Malo, porque exige controles adicionales para evitar consultas demasiado costosas.

### gRPC

Permite una comunicación rápida y estructurada principalmente entre servicios.

* Bien, porque ofrece un buen rendimiento en comunicaciones internas.
* Bien, porque define de forma estricta los mensajes intercambiados.
* Malo, porque está orientado principalmente a la comunicación entre servicios y MyComplex utiliza actualmente un monolito modular.
* Malo, porque agrega herramientas y formatos que no son necesarios para la comunicación principal entre navegador y backend.

### WebSockets

Mantiene una conexión abierta entre el cliente y el servidor para enviar información en ambas direcciones en tiempo real.

* Bien, porque permite una comunicación inmediata y continua.
* Bien, porque puede utilizarse en funciones que requieren interacción en tiempo real.
* Malo, porque exige mantener conexiones abiertas y aumenta la complejidad del backend.
* Malo, porque las notificaciones en tiempo real de MyComplex ya serán atendidas mediante Firebase Cloud Messaging.

### Server-Sent Events (SSE)

Mantiene una conexión desde el servidor hacia el cliente para enviar actualizaciones continuas.

* Bien, porque es más sencillo que WebSockets cuando únicamente el servidor necesita enviar actualizaciones.
* Bien, porque puede ser útil para paneles o procesos que cambian constantemente.
* Malo, porque MyComplex no tiene actualmente una función que requiera un flujo continuo de información desde el backend.
* Malo, porque añadiría otro mecanismo de comunicación que el equipo tendría que mantener.

## Enlaces

* Relacionado con la arquitectura del backend basada en Monolito Modular y Arquitectura Hexagonal.
* Relacionado con la decisión de utilizar Java y Spring Boot.
* Relacionado con la autenticación mediante Auth0.
* Relacionado con la autorización y el aislamiento de información por `TenantId`.
* Relacionado con el servicio de notificaciones push mediante Firebase Cloud Messaging.
