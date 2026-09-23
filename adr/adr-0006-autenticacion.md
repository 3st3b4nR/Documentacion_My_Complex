# implementar la autenticacion de usuarios
* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-16

## Contexto y planteamiento del problema

MyComplex requiere verificar de manera segura la identidad de los usuarios. Establecimos no desarrollar el sistema de autenticación desde cero, ya que gestionar contraseñas manualmente implica un riesgo de seguridad crítico y un esfuerzo que desvía al equipo de la lógica de negocio central. Por lo tanto, se utilizará una plataforma especializada. Considerando que el sistema opera bajo un modelo multi-tenant y requiere obligatoriamente que el identificador del conjunto residencial (`TenantId`) y el rol del usuario viajen dentro del token de acceso, ¿qué plataforma externa resulta más conveniente para MyComplex garantizando máxima seguridad y menor carga operativa?

## Impulsores de decisión 

* Mitigar los riesgos de seguridad asociados con la gestión y almacenamiento manual de credenciales.
* Facilitar la inyección ágil de atributos personalizados (`TenantId` y Rol) dentro del token generado.
* Minimizar la carga de mantenimiento de infraestructura (complejidad operativa) para el equipo actual.
* Asegurar una integración estandarizada y estable con la arquitectura del backend.

## Opciones consideradas

* Auth0 (Plataforma de identidad corporativa)
* Firebase Authentication (Plataforma de identidad de Google)
* Keycloak (Servidor de identidad autohospedado)

## Resultado de la decisión

Opción elegida: **"Auth"**, porque ofrece el mejor equilibrio entre velocidad de integración, adopción de estándares empresariales y facilidad para resolver el modelo multi-tenant. 

A través de su funcionalidad de acciones de inicio de sesión (Auth0 Actions), la plataforma permite interceptar el flujo de autenticación e inyectar el identificador del conjunto residencial (`TenantId`) y el rol del usuario directamente en el token JWT. Esto satisface el requisito técnico más crítico para que el backend autorice las peticiones sin requerir que el equipo invierta tiempo gestionando infraestructura adicional.

### Consecuencias positivas 

* Se elimina el riesgo de vulnerabilidades por almacenar contraseñas en bases de datos propias.
* Se resuelve el aislamiento de datos multi-tenant en la capa de autenticación de forma nativa y sin servidores intermedios.
* Se acelera el desarrollo gracias a la disponibilidad de integraciones maduras de grado empresarial.

### Consecuencias negativas 

* Se introduce una dependencia técnica estricta hacia un proveedor externo.
* El modelo de precios basado en usuarios activos requiere supervisión a largo plazo y podría exigir ajustes presupuestales ante un escalamiento masivo del sistema.

## Pros y contras de las opciones 

### Auth0

Plataforma de gestión de identidad como servicio (IdPaaS) enfocada en estándares corporativos.

* Bien, porque facilita la inyección nativa del `TenantId` en los tokens mediante reglas personalizadas.
* Bien, porque proporciona una integración robusta y altamente documentada.
* Malo, porque su esquema de costos a largo plazo puede afectar el presupuesto operativo en un escenario de hipercrecimiento.

### Firebase Authentication

Servicio de identidad gestionado perteneciente al ecosistema de Google Cloud.

* Bien, porque su capa gratuita es inmensa para métodos de autenticación con contraseña.
* Bien, porque permitiría centralizar las dependencias externas si se utiliza en conjunto con Firebase Cloud Messaging (FCM).
* Malo, porque inyectar atributos personalizados (`TenantId`) en el token exige desarrollar y mantener funciones intermedias en la nube (Cloud Functions), añadiendo fricción al desarrollo.

### Keycloak

Servidor de gestión de identidad de código abierto desarrollado por Red Hat.

* Bien, porque es gratuito y no genera costos adicionales sin importar el volumen de usuarios.
* Bien, porque otorga control absoluto sobre los datos de identidad sin depender de terceros.
* Malo, porque requiere desplegar, asegurar y mantener servidores y bases de datos dedicados, elevando drásticamente la complejidad operativa.

