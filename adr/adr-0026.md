# definir el comportamiento reactivo de la aplicacion web

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex debe ofrecer una experiencia de uso ágil, especialmente en funciones como registro de visitantes, reservas, noticias y notificaciones. Se debe decidir si toda la aplicación será reactiva, si trabajará de forma tradicional o si se utilizará un enfoque combinado que permita una interfaz dinámica sin aumentar innecesariamente la complejidad del backend.

## Impulsores de decisión

* Mejorar la usabilidad mediante respuestas visuales rápidas ante las acciones del usuario.
* Evitar recargas completas de la página durante las operaciones normales.
* Mantener una experiencia fluida en la PWA y en los diferentes perfiles de usuario.
* Mantener una complejidad de desarrollo adecuada para el tamaño actual del equipo.
* Evitar introducir programación reactiva en el backend si no existe una necesidad real que la justifique.
* Mantener compatibilidad con la operación offline definida para portería.

## Opciones consideradas

* Aplicación no reactiva.
* Aplicación completamente reactiva, incluyendo frontend y backend.
* Enfoque híbrido: frontend reactivo y backend tradicional.

## Resultado de la decisión

Opción elegida: **"Enfoque híbrido: frontend reactivo y backend tradicional"**, porque permite ofrecer una interfaz dinámica y rápida para el usuario sin obligar al backend a utilizar un modelo de programación más complejo del que actualmente necesita MyComplex.

El frontend podrá actualizar partes de la interfaz sin recargar toda la página, mostrar estados de carga, reflejar cambios de información y responder de forma inmediata a las acciones del usuario. El backend continuará atendiendo las solicitudes de manera tradicional mediante Spring Boot, mientras que las notificaciones en tiempo real seguirán siendo atendidas por Firebase Cloud Messaging.

### Consecuencias positivas

* Mejora la experiencia del usuario al reducir recargas completas de la página.
* Permite mostrar cambios, mensajes y estados de manera inmediata en la interfaz.
* Mantiene el backend más sencillo de desarrollar y mantener.
* Evita adoptar programación reactiva en todo el sistema sin una necesidad concreta.
* Se adapta bien al funcionamiento de la PWA y a la estrategia offline ya definida.
* Permite incorporar comportamientos en tiempo real únicamente donde sean necesarios.

### Consecuencias negativas

* El frontend deberá manejar estados de carga, errores y actualizaciones de información.
* El equipo deberá mantener una separación clara entre el comportamiento dinámico de la interfaz y el procesamiento del backend.
* Si en el futuro aparecen necesidades de transmisión continua de información, podría ser necesario revisar esta decisión.

## Pros y contras de las opciones

### Aplicación no reactiva

La interfaz dependería principalmente de recargas o actualizaciones completas después de cada operación.

* Bien, porque su implementación inicial es sencilla.
* Bien, porque requiere menos manejo de estados en el frontend.
* Malo, porque ofrece una experiencia menos fluida para el usuario.
* Malo, porque obliga a realizar recargas innecesarias durante operaciones frecuentes.
* Malo, porque aprovecha menos las capacidades de una PWA moderna.

### Aplicación completamente reactiva, incluyendo frontend y backend

Tanto la interfaz como el backend utilizarían un modelo reactivo y no bloqueante.

* Bien, porque puede manejar eficientemente grandes cantidades de conexiones simultáneas.
* Bien, porque permite construir flujos de información en tiempo real.
* Malo, porque aumenta considerablemente la complejidad del backend.
* Malo, porque MyComplex no tiene actualmente una necesidad que justifique hacer todo el backend reactivo.
* Malo, porque aumenta la curva de aprendizaje y el esfuerzo de mantenimiento del equipo.

### Enfoque híbrido: frontend reactivo y backend tradicional

La interfaz funciona de manera dinámica, mientras el backend mantiene un modelo tradicional de solicitud y respuesta.

* Bien, porque mejora la usabilidad sin aumentar innecesariamente la complejidad del backend.
* Bien, porque se adapta bien a una PWA y a las operaciones habituales de MyComplex.
* Bien, porque permite usar servicios especializados como Firebase para los casos que realmente requieren notificaciones en tiempo real.
* Bien, porque mantiene una arquitectura sencilla y adecuada para el tamaño actual del equipo.
* Malo, porque requiere manejar correctamente los estados de la interfaz y la sincronización de información.

## Enlaces

* Relacionado con la estrategia de operación offline para portería.
* Relacionado con la arquitectura del backend basada en Monolito Modular y Arquitectura Hexagonal.
* Relacionado con el servicio de notificaciones push mediante Firebase Cloud Messaging.
* Relacionado con el atributo de calidad de usabilidad.
