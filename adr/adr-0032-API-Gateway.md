# centralizar el acceso a las api de mycomplex mediante un api gateway

* Estado: propuesto
* Fecha: 2026-10-13

## Contexto y planteamiento del problema

MyComplex es una plataforma para la administración de conjuntos residenciales que permite gestionar usuarios, visitantes, reservas de zonas comunes, noticias, reuniones, tareas y otras operaciones administrativas. El sistema contará con una aplicación web y un backend desarrollado en Java con Spring Boot.

Actualmente, las solicitudes de la aplicación deben llegar al backend para ejecutar las funcionalidades correspondientes. A medida que MyComplex incorpore nuevos módulos y aumente el número de conjuntos residenciales que utilizan la plataforma, será necesario mantener un mecanismo organizado para gestionar el acceso a las API, controlar las solicitudes y facilitar la evolución de la arquitectura.

Sin un punto de entrada definido, algunas responsabilidades comunes, como el enrutamiento, el control de tráfico y el registro de solicitudes, podrían quedar distribuidas entre diferentes partes del sistema. Esto aumentaría el esfuerzo de mantenimiento y dificultaría la aplicación uniforme de determinadas políticas.

Aunque inicialmente MyComplex pueda operar con un único backend, se busca establecer una solución que permita administrar las solicitudes de manera centralizada sin obligar a separar los módulos en servicios independientes.

**¿Qué solución de API Gateway resulta más conveniente para centralizar el acceso a las API de MyComplex, facilitar la gestión de solicitudes y permitir la evolución de su arquitectura?**

## Impulsores de decisión

* Centralizar el acceso a las API mediante un punto de entrada definido.
* Facilitar el enrutamiento de las solicitudes hacia las funcionalidades correspondientes.
* Permitir la aplicación de políticas comunes de seguridad y control de tráfico.
* Mantener la compatibilidad con Java y Spring Boot.
* Reducir la complejidad de implementación y mantenimiento para el equipo de desarrollo.
* Facilitar la incorporación de nuevos servicios si la arquitectura evoluciona.
* Mantener costos de infraestructura y operación acordes con el alcance del proyecto.
* Permitir el despliegue de la solución en la infraestructura de Azure prevista para MyComplex.

## Opciones consideradas

### Opción 1: Spring Cloud Gateway

Implementar un API Gateway utilizando Spring Cloud Gateway, una solución del ecosistema Spring que permite gestionar rutas y aplicar filtros a las solicitudes antes de enviarlas al backend.

El gateway se desarrollaría como una aplicación independiente de Spring Boot, con su propia configuración y despliegue.

### Opción 2: Kong Gateway

Implementar Kong Gateway como un componente independiente encargado de recibir las solicitudes y dirigirlas hacia las API correspondientes.

Kong ofrece mecanismos de enrutamiento, autenticación, control de tráfico y otras capacidades mediante plugins configurables.

### Opción 3: NGINX

Utilizar NGINX como proxy inverso para recibir las solicitudes de los clientes y dirigirlas hacia el backend de MyComplex.

Esta alternativa permite centralizar el acceso, configurar rutas y distribuir solicitudes entre diferentes instancias del backend.

## Resultado de la decisión

**Spring Cloud Gateway**

La principal razón es su integración con Java y Spring Boot, tecnologías seleccionadas para el desarrollo del backend. Esto permite que el equipo implemente y mantenga el gateway utilizando herramientas y conocimientos relacionados con los que ya empleará en el proyecto.

Spring Cloud Gateway permite definir rutas, aplicar filtros, limitar solicitudes y establecer mecanismos de supervisión. Estas capacidades resultan convenientes para centralizar responsabilidades relacionadas con la entrada de solicitudes al sistema.

El gateway se implementará como una aplicación Spring Boot independiente del backend principal. Inicialmente, dirigirá las solicitudes hacia las API existentes y permitirá incorporar nuevas rutas si posteriormente se agregan otros servicios.

Las validaciones de permisos, las reglas de negocio y el aislamiento de información entre conjuntos residenciales seguirán siendo responsabilidad del backend. El gateway podrá realizar verificaciones generales de seguridad, pero no reemplazará estos controles.

La incorporación de este componente deberá validarse mediante una prueba de concepto que compruebe su funcionamiento, su integración con el backend y el impacto que introduce sobre las solicitudes.

## Consecuencias

### Consecuencias positivas

* Las API del sistema podrán exponerse mediante un punto de entrada común.
* Se facilitará la organización y modificación de las rutas de acceso.
* Será posible aplicar políticas comunes de control de tráfico.
* Se podrán registrar y supervisar las solicitudes que atraviesen el gateway.
* Se mantendrá una integración tecnológica coherente con Spring Boot.
* Se facilitará la incorporación de servicios adicionales sin modificar necesariamente la forma en que el frontend accede al sistema.
* Se reducirá la necesidad de repetir determinadas configuraciones transversales en diferentes puntos de entrada.

### Consecuencias negativas

* Se incorporará un componente adicional que deberá desarrollarse, configurarse, desplegarse y mantenerse.
* Las solicitudes pasarán por una etapa adicional de procesamiento, lo que puede incrementar el tiempo de respuesta.
* Una falla del gateway podría impedir el acceso a las API, incluso cuando el backend continúe funcionando.
* Será necesario coordinar las versiones compatibles de Spring Boot y Spring Cloud.
* Se requerirán pruebas adicionales de seguridad, enrutamiento y disponibilidad.
* El gateway consumirá recursos de infraestructura adicionales.

## Pros y contras de las opciones

### Spring Cloud Gateway

**Pros**

* Integración directa con el ecosistema Spring.
* Permite utilizar Java para implementar configuraciones y filtros personalizados.
* Ofrece mecanismos de enrutamiento, limitación de solicitudes y manejo de fallos.
* Facilita el trabajo del equipo al utilizar tecnologías relacionadas con el backend.
* Permite incorporar nuevas rutas y servicios conforme evolucione MyComplex.

**Contras**

* Requiere desarrollar y mantener una aplicación adicional.
* Introduce consumo adicional de memoria y procesamiento.
* Requiere verificar la compatibilidad entre versiones de Spring Boot y Spring Cloud.
* Las configuraciones personalizadas pueden incrementar la complejidad del código.

### Kong Gateway

**Pros**

* Solución especializada en la administración de API.
* Dispone de plugins para incorporar funcionalidades de seguridad y control de tráfico.
* Puede utilizarse independientemente de la tecnología del backend.
* Ofrece capacidades de gestión de rutas y supervisión.

**Contras**

* Introduce una tecnología adicional distinta del entorno Java utilizado por el equipo.
* Requiere aprender sus mecanismos de configuración y administración.
* Algunas capacidades avanzadas dependen de ediciones comerciales o servicios adicionales.
* Su conjunto de herramientas puede resultar más amplio de lo necesario para el alcance inicial de MyComplex.

### NGINX

**Pros**

* Permite centralizar solicitudes mediante un proxy inverso.
* Ofrece mecanismos de enrutamiento y balanceo de carga.
* Puede trabajar con aplicaciones desarrolladas en diferentes tecnologías.
* Es una alternativa consolidada para gestionar tráfico HTTP.

**Contras**

* Las políticas avanzadas de administración de API pueden requerir configuraciones adicionales u otros componentes.
* Las personalizaciones no se desarrollan directamente mediante el entorno Java del proyecto.
* Puede aumentar el esfuerzo de mantenimiento cuando se necesitan múltiples reglas de procesamiento de solicitudes.
* Su enfoque como proxy inverso no proporciona por sí solo todas las capacidades de una plataforma especializada de gestión de API.

## Enlaces

* Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
* Documentación de Spring Cloud Gateway: https://docs.spring.io/spring-cloud-gateway/reference/
* Kong Gateway: https://developer.konghq.com/gateway/
* NGINX Reverse Proxy: https://docs.nginx.com/nginx/admin-guide/web-server/reverse-proxy
* Azure Container Apps: https://learn.microsoft.com/azure/container-apps/overview
