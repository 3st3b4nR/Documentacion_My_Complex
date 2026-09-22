# proteger contrasenas claves y credenciales

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex necesitará usar contraseñas, claves de servicios y otras credenciales para conectarse con PostgreSQL, Resend, Firebase y otros recursos. Esta información no debe quedar escrita directamente en el código ni almacenada en el repositorio del proyecto. Se debe definir una forma segura y centralizada de guardarla.

## Impulsores de decisión

* Evitar que contraseñas y claves queden expuestas en el código fuente.
* Controlar qué servicios pueden consultar cada secreto.
* Facilitar el cambio de credenciales cuando sea necesario.
* Utilizar una solución administrada que reduzca el trabajo del equipo.

## Opciones consideradas

* Azure Key Vault
* HashiCorp Vault administrado por el equipo
* Variables de entorno configuradas manualmente sin un almacén central de secretos

## Resultado de la decisión

Opción elegida: **"Azure Key Vault"**, porque permite guardar de forma centralizada contraseñas, claves y otras credenciales, controlar quién puede consultarlas y evitar que estos datos sensibles se incluyan dentro del código de MyComplex. Además, se integra con los servicios de Azure elegidos para la plataforma.

### Consecuencias positivas

* Las credenciales dejan de estar almacenadas directamente en el código.
* Se puede controlar qué servicio tiene acceso a cada secreto.
* Se facilita cambiar o revocar credenciales sin modificar el código fuente.
* Se mantiene un registro más claro de los accesos a información sensible.

### Consecuencias negativas

* Se agrega dependencia de Azure para la gestión de secretos.
* Una configuración incorrecta de permisos podría impedir el acceso legítimo o permitir accesos excesivos.
* El servicio genera un costo adicional, aunque normalmente bajo para el volumen esperado del proyecto.

## Pros y contras de las opciones

### Azure Key Vault

Servicio administrado de Azure para guardar claves, contraseñas y certificados.

* Bien, porque evita guardar secretos en el repositorio.
* Bien, porque se integra con identidades y permisos de Azure.
* Bien, porque reduce el trabajo de mantener un sistema propio de secretos.
* Malo, porque aumenta la dependencia de Azure.

### HashiCorp Vault administrado por el equipo

Herramienta especializada que el equipo puede instalar y controlar directamente.

* Bien, porque ofrece funciones avanzadas y no obliga a usar un único proveedor cloud.
* Bien, porque permite un alto nivel de control.
* Malo, porque requiere instalar, actualizar, asegurar y mantener el servicio.
* Malo, porque su complejidad es mayor de la necesaria para el tamaño actual del proyecto.

### Variables de entorno configuradas manualmente sin un almacén central de secretos

Consiste en guardar las credenciales directamente en la configuración de cada ambiente.

* Bien, porque es sencillo para entornos pequeños de desarrollo.
* Bien, porque no requiere contratar otro servicio.
* Malo, porque dificulta controlar y cambiar credenciales de forma ordenada.
* Malo, porque aumenta el riesgo de copiar o exponer secretos accidentalmente.

## Enlaces

* Relacionado con el ADR 26 de control de acceso entre servicios.
* Relacionado con los ADR de PostgreSQL, Resend, Firebase y almacenamiento de imágenes.
