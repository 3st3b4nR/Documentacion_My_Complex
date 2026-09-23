# alojar y proteger la base de datos postgresql

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex ya definió PostgreSQL como su base de datos principal, pero todavía debe decidirse cómo funcionará en producción y cómo se protegerá la información ante fallos o eliminaciones accidentales. El equipo necesita una solución que reduzca el mantenimiento manual y que incluya mecanismos de respaldo y recuperación sin tener que construirlos desde cero.

## Impulsores de decisión

* Mantener PostgreSQL disponible sin administrar manualmente un servidor.
* Reducir las tareas de instalación, actualización y mantenimiento de la base de datos.
* Contar con copias de seguridad automáticas.
* Poder recuperar la información a un momento anterior cuando ocurra un error o pérdida de datos.
* Mantener una solución que pueda crecer junto con MyComplex.
* Reducir la carga técnica para un equipo pequeño.

## Opciones consideradas

* Azure Database for PostgreSQL Flexible Server con respaldos administrados.
* PostgreSQL instalado en una máquina virtual con respaldos configurados por el equipo.
* PostgreSQL ejecutado en un contenedor con respaldos administrados por el equipo.

## Resultado de la decisión

Opción elegida: **"Azure Database for PostgreSQL Flexible Server con respaldos administrados"**, porque permite utilizar PostgreSQL sin que el equipo tenga que mantener directamente el servidor donde se ejecuta. Además, el servicio realiza respaldos automáticos y permite recuperar la base de datos a un momento anterior dentro del periodo de conservación configurado, cubriendo en una sola decisión tanto la operación de PostgreSQL como la recuperación básica de la información.

### Consecuencias positivas

* El equipo dedica menos tiempo a instalar, actualizar y mantener PostgreSQL.
* Las copias de seguridad se realizan automáticamente.
* Se puede recuperar la base de datos después de errores o eliminaciones accidentales.
* Se reduce el riesgo de olvidar realizar respaldos manuales.
* La capacidad de la base de datos puede ajustarse cuando MyComplex crezca.

### Consecuencias negativas

* El servicio genera un costo mensual de infraestructura.
* MyComplex dependerá de Azure para la operación de la base de datos.
* El equipo deberá definir cuánto tiempo conservar los respaldos y probar periódicamente que la recuperación funciona.
* Una restauración crea una nueva instancia de la base de datos, por lo que se debe validar el procedimiento antes de utilizarlo en una situación real.

## Pros y contras de las opciones

### Azure Database for PostgreSQL Flexible Server con respaldos administrados

Servicio administrado que ejecuta PostgreSQL y se encarga de tareas comunes de mantenimiento y respaldo.

* Bien, porque reduce considerablemente el trabajo operativo del equipo.
* Bien, porque realiza respaldos automáticos sin depender de una persona.
* Bien, porque permite recuperar la base de datos a un momento anterior.
* Bien, porque facilita aumentar recursos cuando el sistema crezca.
* Malo, porque tiene un costo recurrente.
* Malo, porque aumenta la dependencia de Azure.

### PostgreSQL instalado en una máquina virtual con respaldos configurados por el equipo

El equipo instala PostgreSQL en un servidor virtual y configura sus propias tareas de respaldo.

* Bien, porque ofrece mayor control sobre la instalación y configuración.
* Bien, porque permite decidir dónde guardar las copias.
* Malo, porque el equipo debe encargarse de actualizaciones, seguridad, respaldos y restauraciones.
* Malo, porque un error de administración puede afectar directamente la información de MyComplex.

### PostgreSQL ejecutado en un contenedor con respaldos administrados por el equipo

La base de datos se ejecuta en un contenedor y el equipo se responsabiliza por conservar y recuperar los datos.

* Bien, porque resulta práctico para ambientes locales y de pruebas.
* Bien, porque la configuración puede repetirse fácilmente.
* Malo, porque en producción el equipo seguiría siendo responsable del almacenamiento y los respaldos.
* Malo, porque aumenta el riesgo operativo frente a un servicio especializado.

## Enlaces

* Refina el ADR 01, que seleccionó PostgreSQL como gestor de base de datos.
* Reemplaza la necesidad de mantener un ADR independiente para respaldo y recuperación.
* Relacionado con el ADR 25 sobre protección del acceso a los servicios internos.
