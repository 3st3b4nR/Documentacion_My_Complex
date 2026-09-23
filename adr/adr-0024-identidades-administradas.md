# controlar que servicios pueden acceder a otros recursos

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Auth0 controla quiénes son los usuarios de MyComplex, pero los servicios internos también necesitan identificarse entre ellos. Por ejemplo, el backend debe poder leer ciertos secretos o guardar imágenes, mientras otros componentes no deberían tener esos permisos. Se necesita una forma de controlar estos accesos sin repartir claves y contraseñas entre todos los servicios.

## Impulsores de decisión

* Dar a cada servicio únicamente los permisos que realmente necesita.
* Reducir el uso de contraseñas y claves permanentes entre servicios.
* Facilitar la revocación y revisión de permisos.
* Mantener separados los permisos de los usuarios de los permisos de la infraestructura.

## Opciones consideradas

* Microsoft Entra ID Managed Identities con Azure RBAC
* Service Principals con secretos o certificados administrados manualmente
* Credenciales estáticas diferentes para cada servicio

## Resultado de la decisión

Opción elegida: **"Microsoft Entra ID Managed Identities con Azure RBAC"**, porque permite que los servicios de Azure tengan una identidad propia y reciban solo los permisos que necesitan sin guardar contraseñas permanentes en el código. Esto facilita controlar, revisar y retirar accesos desde un mismo lugar y se integra con Key Vault, Storage y Container Registry.

### Consecuencias positivas

* Se reduce la cantidad de contraseñas y claves que deben guardarse.
* Cada servicio puede recibir únicamente los permisos necesarios.
* Los accesos pueden revisarse y retirarse de forma centralizada.
* Se integra con los servicios Azure seleccionados para MyComplex.

### Consecuencias negativas

* El equipo deberá aprender a configurar correctamente identidades y permisos en Azure.
* Una asignación de permisos demasiado amplia podría dar acceso innecesario a un servicio.
* La solución depende del sistema de identidad de Azure.

## Pros y contras de las opciones

### Microsoft Entra ID Managed Identities con Azure RBAC

Permite que los servicios tengan una identidad propia dentro de Azure y reciban permisos específicos.

* Bien, porque evita guardar muchas credenciales permanentes.
* Bien, porque permite limitar los permisos de cada servicio.
* Bien, porque facilita revisar y retirar accesos.
* Malo, porque requiere aprender la configuración de permisos de Azure.
* Malo, porque aumenta la dependencia del proveedor cloud.

### Service Principals con secretos o certificados administrados manualmente

Identidades creadas para aplicaciones que utilizan una clave o certificado propio.

* Bien, porque funcionan en automatizaciones y escenarios donde una identidad administrada no está disponible.
* Bien, porque permiten separar claramente las identidades de las aplicaciones.
* Malo, porque el equipo debe guardar, renovar y proteger sus credenciales.
* Malo, porque una credencial vencida o filtrada puede interrumpir o comprometer el servicio.

### Credenciales estáticas diferentes para cada servicio

Cada aplicación utiliza claves o contraseñas guardadas y distribuidas manualmente.

* Bien, porque es sencillo de entender al inicio.
* Bien, porque funciona sin una plataforma de identidad avanzada.
* Malo, porque aumenta la cantidad de secretos que deben protegerse.
* Malo, porque dificulta controlar y revisar los permisos de manera centralizada.

## Enlaces


* Validación principal: [PoC ADR-0020/0021/0023/0024 — Infraestructura Azure](../PoC/poc-020-024-infraestructura-azure/README.md).
* Relacionado con el ADR 19 de protección de secretos y credenciales.
* Relacionado con el ADR 20 de Container Registry.
* Relacionado con el ADR 13 de almacenamiento de imágenes.
* Complementa los ADR 06 y 07, que gestionan la identidad y los permisos de los usuarios finales.
