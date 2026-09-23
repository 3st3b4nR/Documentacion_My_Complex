# implementar el control de concurrencia para operaciones criticas

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-08

## Contexto y planteamiento del problema

MyComplex procesará solicitudes simultáneas sobre recursos compartidos, siendo el caso más crítico la reserva de zonas comunes (como el salón social o zonas BBQ) por parte de residentes que podrían intentar apartar el mismo espacio en horarios coincidentes. Se debe garantizar la consistencia de los datos y evitar condiciones de carrera sin degradar el rendimiento general del sistema. ¿Qué estrategia y mecanismo de control de concurrencia resulta más conveniente para MyComplex?

## Impulsores de decisión

* Garantizar la integridad y consistencia de los datos ante solicitudes simultáneas de reserva.
* Prevenir condiciones de carrera y reservas duplicadas o incompatibles sobre un mismo recurso.
* Mantener un alto rendimiento del sistema sin generar bloqueos prolongados en la base de datos.
* Ofrecer una experiencia de usuario adecuada al notificar conflictos de forma limpia.
* Permitir la validación de la alternativa mediante una PoC o Spike técnica con el ORM y motor de base de datos.

## Opciones consideradas

* Control pesimista mediante bloqueos explícitos a nivel de base de datos (Pessimistic Locking / `SELECT ... FOR UPDATE`).
* Control optimista mediante tokens de versión o concurrencia gestionados por el ORM (Optimistic Concurrency Control / `RowVersion`).
* Restricciones de unicidad y exclusión temporal a nivel de esquema de base de datos (Database-level Constraints).

## Resultado de la decisión

Opción elegida: **"Control optimista mediante tokens de versión o concurrencia gestionados por el ORM (Optimistic Concurrency Control / `RowVersion`)"**, porque es la opción que mejor se adapta a MyComplex al permitir un flujo de operaciones rápido y sin bloqueos de filas en la base de datos. 

Utiliza un campo de control de versión gestionado por el ORM para detectar si otro usuario modificó el registro en el último milisegundo antes de confirmar la transacción, resolviendo los conflictos limpiamente en la aplicación y evitando los cuellos de botella del bloqueo pesimista.

### Consecuencias positivas

* Se previene de forma efectiva la superposición de reservas en zonas comunes de los conjuntos residenciales.
* Se mantiene un alto rendimiento y escalabilidad al no mantener bloqueos prolongados sobre las filas de la base de datos.
* Se detectan de forma oportuna las modificaciones simultáneas mediante el control de versiones del ORM.
* Se informa de manera clara al usuario cuando ocurre un conflicto para que pueda actualizar su operación.

### Consecuencias negativas

* Si dos usuarios intentan reservar exactamente al mismo tiempo, uno de ellos recibirá una excepción de concurrencia y deberá repetir la acción.
* Será necesario implementar la lógica de captura de excepciones de concurrencia en la capa de aplicación.

## Pros y contras de las opciones

### Control pesimista mediante bloqueos explícitos a nivel de base de datos (Pessimistic Locking / `SELECT ... FOR UPDATE`)

Consiste en bloquear físicamente los registros en el motor de base de datos durante todo el tiempo que dura la lectura y la escritura.

* Bien, porque impide de forma absoluta que otros usuarios modifiquen el recurso mientras está bloqueado.
* Malo, porque genera cuellos de botella severos de rendimiento y aumenta significativamente el riesgo de interbloqueos (*deadlocks*).

Para MyComplex, esta opción es poco conveniente porque degrada la disponibilidad y la experiencia del usuario ante múltiples interacciones concurrentes.

### Control optimista mediante tokens de versión o concurrencia gestionados por el ORM (Optimistic Concurrency Control / `RowVersion`)

Consiste en permitir que las transacciones fluyan sin bloqueos e incluir una columna de versión en la entidad; al persistir, el ORM valida que la versión no haya cambiado.

* Bien, porque mantiene la alta velocidad del sistema al no bloquear filas de la base de datos.
* Bien, porque detecta eficientemente si un recurso fue alterado por otro usuario antes de guardar los cambios.
* Bien, porque su viabilidad puede medirse directamente mediante una PoC utilizando el ORM del proyecto.
* Malo, porque requiere manejar en la aplicación la excepción generada cuando se detecta un conflicto de versión.

Para MyComplex, esta es la opción más conveniente porque equilibra un rendimiento fluido con la prevención estricta de conflictos en las reservas.

### Restricciones de unicidad y exclusión temporal a nivel de esquema de base de datos (Database-level Constraints)

Consiste en delegar completamente la validación de solapamientos e integridad a restricciones lógicas y de unicidad estrictas en el motor relacional.

* Bien, porque blinda la integridad de los datos directamente en la base de datos impidiendo estados lógicamente inválidos.
* Malo, porque la complejidad para estructurar restricciones de exclusión temporal varía drásticamente según el motor relacional elegido.
* Malo, porque puede arrojar errores de base de datos poco amigables hacia la interfaz si no se gestionan adecuadamente en el backend.

Para MyComplex, esta opción es insuficiente por sí sola para manejar la concurrencia general de la aplicación, aunque servirá como apoyo secundario para las restricciones críticas de fechas.

## Enlaces

* Relacionado con la estrategia de integridad y consistencia de datos.
* Relacionado con los atributos de calidad de rendimiento, integridad y disponibilidad.