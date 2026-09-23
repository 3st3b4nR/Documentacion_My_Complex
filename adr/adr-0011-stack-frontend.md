# Definir el stack del frontend de MyComplex con Angular y TypeScript

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-16

Historia técnica: seleccionar el stack del frontend considerando los drivers arquitectónicos y los ADR relacionados.

## Contexto y planteamiento del problema

MyComplex requiere una interfaz web responsiva para gestionar visitantes, reservas, noticias, tareas y comunicaciones de sus cinco perfiles de usuario. Debe consumir la API Java/Spring Boot de ADR-0008, complementar el monolito modular con arquitectura hexagonal de ADR-0003 y soportar la PWA con operación offline de portería definida en ADR-0002. ¿Qué stack satisface estas necesidades y facilita entregas incrementales con un equipo reducido?

## Impulsores de decisión

* **Usabilidad y accesibilidad:** navegación consistente, formularios simples y componentes reutilizables para todos los perfiles, con adaptación móvil y mensajes claros (escenarios 06, 07, 14, 15 y 16).
* **Disponibilidad:** registrar visitantes sin conexión, persistir información localmente y sincronizar al recuperar la red, sin depender de servicios secundarios (escenarios 03 y 10; ADR-0002).
* **Seguridad:** integrar autenticación externa, orientar la interfaz por roles y separar datos locales por usuario y conjunto; la autorización permanece en el backend (ADR-0001, ADR-0006 y ADR-0007).
* **Mantenibilidad y crecimiento:** organizar por funcionalidades y separar presentación, API y almacenamiento, en coherencia con SOLID, GRASP y ADR-0003, sin copiar todas las capas del servidor.
* **Confiabilidad:** distinguir operaciones pendientes de confirmadas, recuperar borradores y comunicar conflictos y acciones críticas; auditoría y concurrencia se resuelven en el backend (escenario 19; ADR-0004 y ADR-0005).
* **Rendimiento:** carga diferida, consultas paginadas y compilación optimizada para los objetivos de búsquedas, login y reservas (escenarios 12, 22 y 23).
* **Integraciones:** recibir notificaciones mediante FCM sin interferir con la PWA; el envío de push y correo y las credenciales administrativas pertenecen al backend (ADR-0009 y ADR-0010).
* **Viabilidad:** reducir decisiones de integración y evaluar aprendizaje, mantenimiento y operación, considerando equipo, tiempo, presupuesto y validación antes del despliegue. No se ha documentado experiencia del equipo con los frameworks candidatos.

## Opciones consideradas

* Angular + TypeScript + Angular CLI + Angular Material/CDK.
* React + TypeScript + Vite + Material UI, con React Router, React Hook Form y TanStack Query.
* Vue + TypeScript + Vite + Vuetify, con Vue Router y Pinia.

## Resultado de la decisión

Opción elegida: **"Angular + TypeScript + Angular CLI + Angular Material/CDK"**, porque reúne navegación, formularios, acceso HTTP e inyección de dependencias bajo convenciones comunes. Esto reduce decisiones de integración para un equipo pequeño y facilita mantener consistencia entre portería, reservas y administración. La arquitectura del backend es compatible con las tres opciones; se elige Angular por su conjunto integrado de herramientas, sin asumir superioridad de rendimiento o experiencia previa del equipo.

El stack incluirá **Angular Router** para navegación y carga diferida, **Reactive Forms** para formularios, **HttpClient** para consumir la API REST/JSON sobre HTTPS, y **Signals/RxJS** para estado y flujos asíncronos. Material/CDK y CSS responsivo darán una base común de controles y mensajes. El código se organizará por funcionalidades, con adaptadores para API, identidad y almacenamiento.

La PWA utilizará **IndexedDB mediante idb** para pendientes y borradores, y un **Service Worker personalizado con Workbox** para recursos offline. Se requiere esta implementación porque el worker incorporado de Angular ofrece caché básica y no resuelve la sincronización de negocio. **Firebase Messaging** compartirá el mismo registro del worker para recibir push; el proveedor de autenticación sigue pendiente según ADR-0006.

Los registros offline se guardarán con identificador estable, usuario, conjunto y estado pendiente; se sincronizarán con sesión válida e idempotencia también implementada en el backend. Solo la respuesta del servidor permitirá marcarlos como confirmados. Los datos locales tendrán separación y retención controladas en dispositivos compartidos; la política de sesión offline debe definirse y un registro pendiente no equivale a autorización de ingreso.

### Consecuencias positivas

* Herramientas comunes reducen el esfuerzo de integración y facilitan entregas incrementales.
* Componentes reutilizables favorecen una experiencia consistente entre perfiles y dispositivos.
* Adaptadores y organización por funcionalidades facilitan pruebas y mantenimiento.
* Una base de código web conserva la estrategia PWA y puede distribuirse como archivos estáticos por HTTPS.

### Consecuencias negativas

* El equipo debe aprender Angular, TypeScript, RxJS y el ciclo de vida del Service Worker.
* El worker propio y IndexedDB exigen pruebas, migraciones y cuidado de pendientes; borrar datos del navegador puede causar pérdidas.
* La sincronización depende de contratos del backend para idempotencia, permisos y conflictos.
* Usabilidad, accesibilidad, rendimiento y protección de datos requieren validación; no los garantiza el framework.
* Deben concretarse el proveedor de identidad y la política de sesión y retención offline, además de mantener versiones compatibles del stack.

## Pros y contras de las opciones

### Angular + TypeScript + Angular CLI + Angular Material/CDK

Framework con herramientas integradas para navegación, formularios, HTTP e inyección de dependencias.

* Bien, porque ofrece convenciones comunes para los distintos módulos y perfiles.
* Bien, porque facilita aislar adaptadores y reutilizar controles y validaciones.
* Malo, porque requiere aprender varias abstracciones y mantener compatibilidad entre versiones.
* Malo, porque la operación offline avanzada requiere implementación adicional; Angular no garantiza sincronización ni fronteras de negocio.

### React + TypeScript + Vite + Material UI

Biblioteca de interfaz complementada con herramientas de navegación, formularios y datos remotos.

* Bien, porque permite componer herramientas según las necesidades de la interfaz.
* Bien, porque es compatible con la API Spring Boot y la misma estrategia PWA.
* Malo, porque exige acordar y mantener la integración entre más bibliotecas para los flujos comunes.
* Malo, porque no existe experiencia documentada o una base reutilizable del equipo que compense ese esfuerzo en este proyecto.

### Vue + TypeScript + Vite + Vuetify

Framework de componentes con Vue Router y Pinia para navegación y estado compartido.

* Bien, porque dispone de herramientas oficiales para construcción, navegación y estado.
* Bien, porque permite separar responsabilidades y consumir la misma API del backend.
* Malo, porque requiere acuerdos adicionales para formularios, validación y UI frente al conjunto integrado de Angular.
* Malo, porque su ventaja de aprendizaje para este equipo no está demostrada; la sincronización offline también requiere desarrollo propio.

## Enlaces

* Fuente de requisitos: [Drivers arquitectónicos](../Drivers%20Arquitect%C3%B3nicos.md).
* Condicionan PWA y estructura: [ADR-0002: offline](adr-0002.md), [ADR-0003: arquitectura](adr-003.md) y [ADR-0008: backend](adr-0008.md).
* Condicionan seguridad: [ADR-0001: datos multi-tenant](adr-0001.md), [ADR-0006: autenticación](adr-0006.md) y [ADR-0007: autorización](adr-0007.md).
* Condicionan operaciones: [ADR-0004: trazabilidad](adr-0004.md) y [ADR-0005: concurrencia](adr-0005.md).
* Condicionan integraciones: [ADR-0009: push](adr-0009.md) y [ADR-0010: correo](adr-0010.md).
* Contexto y validación pendiente: [modelo de contexto con API](../Modelo_de_contexto2.drawio) y [correcciones pendientes](../CORRECCIONES%20PENDIENTES.MD). Los ADR relacionados siguen en estado propuesto.
* Fundamentación del framework: [Angular](https://angular.dev/overview), [composición de React](https://react.dev/learn/build-a-react-app-from-scratch) y [herramientas de Vue](https://vuejs.org/guide/scaling-up/tooling.html).
* Fundamentación de PWA y push: [limitaciones del worker de Angular](https://angular.dev/ecosystem/service-workers), [Workbox](https://developer.chrome.com/docs/workbox/modules/workbox-build), [idb](https://github.com/jakearchibald/idb) y [FCM web](https://firebase.google.com/docs/cloud-messaging/web/receive-messages).
