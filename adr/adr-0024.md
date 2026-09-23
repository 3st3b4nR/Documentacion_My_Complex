# restringir el acceso a los servicios internos

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

El backend de MyComplex necesita conectarse con PostgreSQL y otros servicios internos. Aunque una red completamente privada ofrece mayor aislamiento, implementarla desde la etapa actual aumenta los costos y la complejidad del proyecto. Se necesita una estrategia que reduzca la exposición de estos servicios sin introducir una infraestructura difícil de mantener para el equipo.

## Impulsores de decisión

* Evitar que los servicios internos queden abiertos libremente a cualquier conexión desde Internet.
* Proteger la comunicación con la base de datos mediante conexiones cifradas.
* Mantener una solución viable para un equipo pequeño.
* Evitar costos adicionales de redes privadas mientras no sean necesarios.
* Poder aumentar el nivel de aislamiento cuando MyComplex pase a una etapa de producción con mayores requisitos de seguridad.

## Opciones consideradas

* Acceso público restringido mediante firewall y conexiones TLS.
* Red privada de Azure con VNet y conexiones privadas.
* Servicios públicos protegidos únicamente mediante usuario y contraseña.

## Resultado de la decisión

Opción elegida: **"Acceso público restringido mediante firewall y conexiones TLS"**, porque permite proteger la base de datos y otros servicios sin introducir desde el inicio la complejidad y el costo de una red privada completa. Los servicios deberán bloquear por defecto las conexiones no autorizadas, permitir únicamente los orígenes necesarios y utilizar conexiones cifradas.

Esta decisión corresponde a la etapa actual de MyComplex. Antes de operar con clientes reales o cuando el nivel de riesgo aumente, deberá revisarse la conveniencia de migrar PostgreSQL y otros recursos sensibles a una red privada de Azure.

### Consecuencias positivas

* La configuración inicial es más sencilla que una red privada completa.
* Se evitan por ahora costos relacionados con conexiones privadas y componentes adicionales de red.
* PostgreSQL puede bloquear conexiones que no estén permitidas mediante reglas de firewall.
* Las comunicaciones con PostgreSQL se mantienen cifradas mediante TLS.
* La arquitectura puede evolucionar posteriormente hacia conexiones privadas sin cambiar la lógica principal de MyComplex.

### Consecuencias negativas

* Los servicios protegidos mediante este mecanismo continúan teniendo un punto de acceso público.
* Las reglas de firewall deben mantenerse correctamente para no permitir rangos demasiado amplios.
* Algunos servicios en la nube pueden cambiar sus direcciones de salida, lo que puede exigir ajustar la configuración de acceso.
* Esta solución ofrece menos aislamiento que una red privada y deberá reevaluarse antes de una operación de producción más exigente.

## Pros y contras de las opciones

### Acceso público restringido mediante firewall y conexiones TLS

Los servicios utilizan un punto de acceso público, pero bloquean las conexiones no permitidas y cifran la comunicación.

* Bien, porque es más sencillo de implementar y entender para el equipo actual.
* Bien, porque evita agregar desde el inicio servicios de red que generan mayor costo y complejidad.
* Bien, porque Azure Database for PostgreSQL bloquea por defecto las conexiones que no se encuentren permitidas mediante reglas de firewall.
* Bien, porque PostgreSQL exige conexiones protegidas mediante TLS.
* Malo, porque el servicio continúa teniendo un punto de acceso público.
* Malo, porque las reglas de acceso deben revisarse y mantenerse cuidadosamente.

### Red privada de Azure con VNet y conexiones privadas

Los servicios se conectan mediante direcciones privadas y dejan de estar disponibles directamente desde Internet.

* Bien, porque ofrece un mayor nivel de aislamiento.
* Bien, porque reduce la exposición pública de la base de datos y otros servicios sensibles.
* Malo, porque requiere comprender y mantener redes, subredes, resolución de nombres y conexiones privadas.
* Malo, porque algunos componentes de red pueden generar costos adicionales.
* Malo, porque introduce una complejidad mayor de la necesaria para la etapa actual del proyecto.

### Servicios públicos protegidos únicamente mediante usuario y contraseña

Los servicios permanecen disponibles desde Internet y la protección depende principalmente de sus credenciales.

* Bien, porque es la opción más sencilla de configurar.
* Malo, porque aumenta innecesariamente la exposición de recursos sensibles.
* Malo, porque una credencial comprometida tendría un impacto mucho mayor.
* Malo, porque no aplica una segunda barrera de protección mediante reglas de acceso de red.

## Enlaces

* Relacionado con el ADR 21 de PostgreSQL administrado.
* Relacionado con el ADR 19 de protección de secretos.
* Relacionado con el ADR 26 de control de acceso entre servicios.
* Esta decisión debe revisarse antes de una puesta en producción con clientes reales.
