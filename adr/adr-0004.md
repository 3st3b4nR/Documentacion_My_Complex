# conservar la trazabilidad de la informacion y operaciones criticas

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-08

## Contexto y planteamiento del problema

MyComplex gestionará información relacionada con residentes, visitantes, registros de acceso, reservas, comunicados y otras operaciones necesarias para la administración de múltiples conjuntos residenciales. Se debe determinar cómo eliminar o conservar los registros para reducir la pérdida innecesaria de información y mantener la trazabilidad de las operaciones críticas, considerando además el aislamiento de la información entre conjuntos residenciales. ¿Qué estrategia de eliminación, conservación y auditoría resulta más conveniente para MyComplex?

## Impulsores de decisión

* Conservar información histórica necesaria para auditorías y la atención de incidentes (como registros de accesos y reservas pasadas).
* Reducir el riesgo de pérdida permanente de información relevante (datos operacionales que no deben eliminarse por error humano).
* Mantener trazabilidad sobre las operaciones críticas (autorización de visitantes, reservas de zonas comunes y modificaciones administrativas).
* Mantener el aislamiento estricto de la información correspondiente a cada conjunto residencial (`TenantId`).
* Facilitar la investigación de incidentes relacionados con información relevante (como accesos indebidos o disputas en la copropiedad).
* Considerar los principios aplicables al tratamiento de datos personales establecidos en la Ley 1581 de 2012.
* Mantener un equilibrio entre almacenamiento, mantenibilidad y trazabilidad.
* Evitar una solución con una complejidad desproporcionada (descartando arquitecturas de auditoría empresarial masiva o bases de datos especializadas de *Event Sourcing* para la etapa inicial del SaaS).
* Permitir la validación de la alternativa mediante una PoC o Spike técnica con el ORM.

## Opciones consideradas

* Borrado físico mediante eliminación permanente de registros.
* Borrado lógico mediante metadatos de eliminación (Soft Delete).
* Borrado lógico mediante metadatos de eliminación complementado con registro de auditoría para operaciones críticas.

## Resultado de la decisión

Opción elegida: **"Borrado lógico mediante metadatos de eliminación complementado con registro de auditoría para operaciones críticas"**, porque permite conservar información operativa que pueda ser necesaria posteriormente y, al mismo tiempo, mantener evidencia forense de las acciones relevantes realizadas sobre ella, asegurando que tanto los registros inactivos como la auditoría pertenezcan de forma aislada a su respectivo conjunto residencial (`TenantId`).

El borrado lógico permitirá marcar los registros como inactivos mediante metadatos (ej. `IsDeleted` o `DeletedAt`) sin eliminarlos inmediatamente de la base de datos, mientras que el registro de auditoría permitirá conservar información sobre operaciones críticas (el usuario responsable, la fecha, el tipo de operación, el recurso afectado y el conjunto residencial). Su implementación concreta y su impacto sobre las consultas y el rendimiento deberán validarse posteriormente mediante una PoC o Spike.

### Consecuencias positivas

* Se reduce el riesgo de pérdida permanente de información relevante de residentes y visitantes.
* Se facilita la conservación de registros históricos con separación lógica por conjunto residencial.
* Se mejora la trazabilidad de operaciones críticas.
* Se puede identificar quién realizó una operación, cuándo se realizó y sobre qué recurso.
* Se facilita la investigación de incidentes relacionados con registros importantes.
* Se mantiene el aislamiento lógico de la información entre conjuntos residenciales.
* La auditoría puede limitarse a operaciones críticas para evitar registrar información innecesaria y controlar el crecimiento del almacenamiento.

### Consecuencias negativas

* Los registros eliminados lógicamente continúan ocupando espacio de almacenamiento en la base de datos relacional.
* Las consultas normales de la aplicación deberán diferenciar y filtrar correctamente entre registros activos y registros eliminados.
* Será necesario definir qué operaciones se consideran críticas y deben ser auditadas mediante interceptores del ORM.
* Se deberá proteger el registro de auditoría frente a modificaciones o eliminaciones no autorizadas.
* La solución tiene mayor complejidad de implementación y mantenimiento que el borrado físico.

## Pros y contras de las opciones

### Borrado físico mediante eliminación permanente de registros

Consiste en eliminar permanentemente los registros de la base de datos cuando dejan de ser necesarios para la operación normal del sistema.

* Bien, porque simplifica la gestión y las consultas de los registros activos al no requerir filtros adicionales.
* Bien, porque reduce el espacio ocupado por información que ya no se encuentra activa.
* Malo, porque la información eliminada no puede recuperarse mediante el sistema ante un error operativo.
* Malo, porque dificulta la consulta de antecedentes relacionados con visitantes, accesos o reservas pasadas.
* Malo, porque no permite conservar evidencia sobre la información eliminada.
* Malo, porque dificulta la investigación posterior de incidentes en la copropiedad.

Para MyComplex, esta opción resulta poco conveniente debido a que la información gestionada requiere seguimiento, recuperación y consultas históricas.

### Borrado lógico mediante metadatos de eliminación

Consiste en conservar físicamente los registros y marcarlos como eliminados mediante información como un estado de eliminación, fecha de eliminación y el usuario que realizó la acción.

* Bien, porque evita la pérdida inmediata y permanente de información por errores humanos.
* Bien, porque permite conservar registros históricos de manera estructurada.
* Bien, porque facilita la recuperación de registros cuando esté permitido por las políticas del sistema.
* Malo, porque requiere que todas las consultas del backend distingan correctamente los registros activos de los eliminados.
* Malo, porque los registros eliminados continúan ocupando espacio de almacenamiento.
* Malo, porque por sí solo no proporciona una trazabilidad completa de las modificaciones realizadas sobre los registros antes de su baja.

Para MyComplex, esta opción es más conveniente que el borrado físico, pero resulta insuficiente cuando se necesita conservar evidencia detallada de operaciones críticas.

### Borrado lógico mediante metadatos de eliminación complementado con registro de auditoría para operaciones críticas

Consiste en conservar los registros mediante borrado lógico y generar registros centralizados de auditoría para las operaciones consideradas críticas, incluyendo el actor, la acción, el recurso afectado, la fecha y el conjunto residencial correspondiente.

* Bien, porque conserva información que puede ser necesaria para consultas posteriores de administración.
* Bien, porque reduce el riesgo de pérdida accidental de información relevante.
* Bien, porque mejora sustancialmente la trazabilidad de operaciones críticas.
* Bien, porque permite identificar quién realizó determinadas acciones y cuándo fueron realizadas.
* Bien, porque facilita la investigación de incidentes y reclamos dentro del conjunto residencial.
* Bien, porque permite aplicar la auditoría de manera selectiva sobre operaciones relevantes, controlando el impacto en rendimiento.
* Malo, porque requiere mayor capacidad de almacenamiento que el borrado físico.
* Malo, porque aumenta la complejidad de implementación en la capa de persistencia (interceptores del ORM).
* Malo, porque los registros de auditoría deben contar con mecanismos adecuados de protección contra modificaciones.

Para MyComplex, esta es la opción más conveniente porque combina la conservación de información con la trazabilidad de operaciones críticas, manteniendo un nivel de complejidad razonable para el alcance del proyecto.

La alternativa deberá validarse mediante una PoC o Spike que compruebe, como mínimo, el marcado de registros como eliminados, el filtrado automático de registros activos, la consulta controlada de información histórica, la generación de registros de auditoría y el aislamiento de los registros correspondientes a diferentes conjuntos residenciales.

## Enlaces

* Relacionado con la estrategia de manejo de concurrencia.
* Relacionado con la estrategia de autenticación y autorización.
* Relacionado con la estrategia de aislamiento de datos multi-tenant.
* Relacionado con los atributos de calidad de integridad, confiabilidad y seguridad.