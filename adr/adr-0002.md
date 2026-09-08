# encontrar la estrategia de operacion offline para porteria

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-08

## Contexto y planteamiento del problema

Se requiere un mecanismo para que la portería del conjunto residencial pueda seguir registrando visitantes y realizando operaciones críticas incluso cuando existan interrupciones temporales en la conexión a Internet, garantizando que la información se sincronice posteriormente sin pérdida de datos.

## Impulsores de decisión <!-- opcional -->

* Necesidad de mantener la disponibilidad del registro de visitantes ante caídas de red (Escenarios 03 y 10).
* Garantizar la confiabilidad y consistencia de los datos almacenados localmente antes de su sincronización.
* Reducir los costos y esfuerzos de mantenimiento evitando el desarrollo de múltiples aplicaciones nativas independientes.

## Opciones consideradas

* PWA + IndexedDB + Service Worker
* Aplicación Local con SQLite
* LocalStorage + Reintento

## Resultado de la decisión

Opción elegida: "PWA + IndexedDB + Service Worker", porque permite atender el escenario crítico de operación offline en portería utilizando una única aplicación web, evitando el desarrollo de aplicaciones independientes para cada dispositivo. IndexedDB maneja la información estructurada localmente y el Service Worker gestiona los recursos durante la desconexión.

### Consecuencias positivas <!-- opcional -->

* Mantiene una única base de código web, reduciendo el esfuerzo de desarrollo y distribución.
* Garantiza la continuidad operativa de la portería ante interrupciones temporales de Internet, mejorando la disponibilidad.
* Permite el almacenamiento seguro de datos estructurados complejos de manera temporal gracias a IndexedDB.

### Consecuencias negativas <!-- opcional -->

* Existe riesgo de pérdida de información si el usuario o el sistema elimina los datos del navegador antes de completar la sincronización.
* Incrementa la complejidad técnica del frontend, requiriendo implementar identificadores únicos locales, control de estados de sincronización, reintentos, idempotencia y resolución de conflictos.

## Pros y contras de las opciones <!-- opcional -->

### PWA + IndexedDB + Service Worker

Aplicación web progresiva con almacenamiento local avanzado y manejo de procesos en segundo plano.

* Bien, porque ofrece alta experiencia de uso y confiabilidad offline sin salir del entorno web.
* Bien, porque mantiene el costo de desarrollo bajo-medio al centralizar el código.
* Malo, porque exige una implementación meticulosa de sincronización para evitar pérdida o duplicación de datos.

### Aplicación Local con SQLite

Aplicación de escritorio o móvil instalada nativamente con base de datos propia.

* Bien, porque ofrece una confiabilidad offline muy alta.
* Malo, porque representa un alto costo y mayor esfuerzo en desarrollo, distribución y mantenimiento de aplicaciones independientes por plataforma.

### LocalStorage + Reintento

Uso del almacenamiento básico del navegador combinado con reintentos simples de peticiones HTTP.

* Bien, porque su complejidad de implementación es baja.
* Malo, porque presenta limitaciones severas para manejar datos estructurados.
* Malo, porque ofrece una confiabilidad muy baja para recuperación ante interrupciones prolongadas de conectividad.

## Enlaces <!-- opcional -->

* [Documento] Drivers arquitectónicos