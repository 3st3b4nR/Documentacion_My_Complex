# automatizar la integracion y entrega continua

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

MyComplex necesita automatizar la validación y preparación de nuevas versiones para reducir errores manuales durante la integración y publicación del sistema. Se debe seleccionar una herramienta de integración y entrega continua que permita ejecutar procesos automáticos desde el repositorio del proyecto sin obligar al equipo a mantener infraestructura adicional.

## Impulsores de decisión

* Reducir errores humanos durante la integración y publicación de nuevas versiones (Operabilidad).
* Ejecutar validaciones automáticas antes de permitir que cambios defectuosos lleguen a una versión estable (Resiliencia).
* Mantener un historial verificable de las ejecuciones realizadas y sus resultados (Cumplimiento y trazabilidad).
* Integrarse de forma sencilla con el repositorio donde se mantiene el código de MyComplex (Mantenibilidad).
* Evitar administrar servidores dedicados únicamente para automatización (Costo y Operabilidad).
* Proteger credenciales y permisos utilizados por los procesos automáticos (Seguridad).
* Permitir que el proceso pueda crecer progresivamente sin exigir una plataforma sobredimensionada para el tamaño actual del equipo (Costo).

## Opciones consideradas

* GitHub Actions.
* Azure DevOps Pipelines.
* Jenkins.

## Resultado de la decisión

Opción elegida: **"GitHub Actions"**, porque MyComplex mantiene su código en GitHub y esta alternativa permite automatizar validaciones, compilaciones y tareas de entrega directamente desde el repositorio, sin administrar un servidor adicional.

Para el tamaño actual del equipo, esta opción reduce la carga operativa y facilita la trazabilidad de cada ejecución. Además, permite utilizar permisos y secretos controlados para evitar incluir credenciales directamente en los archivos del proyecto.

La herramienta seleccionada deberá ejecutar únicamente los procesos definidos por otros ADR y decisiones del proyecto. Este ADR no define la estrategia de ramas, el esquema de versionado ni la herramienta de pruebas unitarias; estas decisiones se documentan por separado.

### Consecuencias positivas

* Se reduce la cantidad de pasos manuales durante la integración y entrega.
* Las ejecuciones quedan registradas y pueden revisarse posteriormente.
* No se requiere instalar ni mantener un servidor de automatización propio.
* Se integra directamente con el repositorio del proyecto.
* Permite detener una integración cuando alguna validación automática falla.
* Facilita aplicar permisos controlados para procesos de publicación.
* Reduce el costo operativo para un equipo pequeño.

### Consecuencias negativas

* El proyecto dependerá parcialmente de la disponibilidad de GitHub para sus automatizaciones.
* Los archivos de workflow deberán mantenerse actualizados.
* Una configuración incorrecta de permisos puede permitir acciones no deseadas.
* El uso intensivo por encima de los límites incluidos en el plan utilizado podría generar costos.

## Pros y contras de las opciones

### GitHub Actions

Herramienta de automatización integrada directamente con GitHub.

* Bien, porque mantiene el código y la automatización en la misma plataforma.
* Bien, porque no requiere mantener servidores propios.
* Bien, porque facilita la trazabilidad entre cambios y ejecuciones.
* Bien, porque permite gestionar secretos y permisos para los procesos automáticos.
* Malo, porque genera dependencia de GitHub.
* Malo, porque los workflows requieren mantenimiento.

### Azure DevOps Pipelines

Servicio administrado de automatización ofrecido por Microsoft.

* Bien, porque ofrece capacidades completas de integración y entrega.
* Bien, porque se integra con servicios del ecosistema Azure.
* Malo, porque introduciría una plataforma adicional diferente al repositorio principal.
* Malo, porque aumenta la cantidad de herramientas que el equipo debe aprender y administrar.

### Jenkins

Servidor de automatización que puede ser administrado directamente por el equipo.

* Bien, porque ofrece un alto nivel de personalización.
* Bien, porque puede integrarse con una gran cantidad de herramientas.
* Malo, porque requiere instalar, actualizar, asegurar y respaldar el servidor.
* Malo, porque aumenta significativamente la carga operativa para un equipo pequeño.
* Malo, porque introduce un costo de mantenimiento que no aporta una ventaja suficiente para el alcance actual.

## Enlaces

* Relacionado con el ADR de estrategia de ramificación.
* Relacionado con el ADR de estrategia de versionado.
* Relacionado con el ADR de pruebas unitarias.
* Relacionado con el ADR de empaquetado del backend en contenedores.
* Relacionado con el ADR de registro de imágenes de contenedor.
