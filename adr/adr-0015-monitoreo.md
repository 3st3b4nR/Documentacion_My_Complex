# supervisar el funcionamiento de mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Cuando MyComplex esté en uso será necesario saber si la plataforma funciona correctamente, si presenta errores o si alguna parte está respondiendo más lento de lo esperado. Se debe definir una forma de recoger esta información y mostrarla de manera clara para poder detectar y resolver problemas con mayor rapidez.

## Impulsores de decisión

* Detectar errores y fallos de la aplicación.
* Identificar operaciones lentas o problemas de rendimiento.
* Facilitar la investigación de incidentes.
* Contar con información sobre el estado general del backend y sus servicios externos.

## Opciones consideradas

* OpenTelemetry con Azure Monitor y Application Insights
* Datadog
* Sentry complementado con registros básicos del proveedor cloud

## Resultado de la decisión

Opción elegida: **"OpenTelemetry con Azure Monitor y Application Insights"**, porque permite que MyComplex genere información sobre errores, tiempos de respuesta y funcionamiento general utilizando un estándar abierto, mientras Azure se encarga de recopilar y mostrar esa información. La solución se integra con Spring Boot y con la infraestructura seleccionada para el proyecto sin obligar al equipo a mantener servidores de monitoreo propios.

### Consecuencias positivas

* Se pueden detectar errores y problemas de rendimiento con mayor rapidez.
* Se facilita conocer qué parte de una operación presentó un fallo.
* El equipo puede crear alertas y paneles para revisar el estado del sistema.
* OpenTelemetry permite mantener una forma estándar de generar la información de monitoreo.

### Consecuencias negativas

* Se debe configurar correctamente qué información se registra para no guardar datos personales innecesarios.
* El almacenamiento de grandes cantidades de registros puede aumentar los costos.
* El equipo deberá aprender a interpretar los paneles, alertas y registros generados.

## Pros y contras de las opciones

### OpenTelemetry con Azure Monitor y Application Insights

OpenTelemetry recoge información del funcionamiento de la aplicación y Azure la almacena y presenta para su análisis.

* Bien, porque se integra con Spring Boot y con los servicios Azure seleccionados.
* Bien, porque permite revisar errores, tiempos de respuesta y comportamiento de las solicitudes.
* Bien, porque evita administrar una plataforma de monitoreo propia.
* Malo, porque el uso intensivo de registros puede aumentar el costo.
* Malo, porque requiere una configuración inicial cuidadosa.

### Datadog

Plataforma externa especializada en monitoreo y observabilidad.

* Bien, porque ofrece herramientas muy completas y paneles avanzados.
* Bien, porque permite supervisar aplicaciones e infraestructura desde un mismo lugar.
* Malo, porque puede representar un costo alto a medida que aumenta el uso.
* Malo, porque añade otro proveedor externo al proyecto.

### Sentry complementado con registros básicos del proveedor cloud

Sentry se centra principalmente en errores de aplicaciones y se complementa con otros registros de infraestructura.

* Bien, porque facilita encontrar errores del código y conocer dónde ocurrieron.
* Bien, porque tiene una integración sencilla con aplicaciones web y backend.
* Malo, porque por sí solo no cubre todo el monitoreo de infraestructura y rendimiento.
* Malo, porque obliga a consultar información en más de una herramienta.

## Enlaces

* Relacionado con el ADR 08 de Java y Spring Boot.
* Relacionado con el ADR 17 de administración de contenedores.
* Relacionado con el ADR 12 de protección de la aplicación.
