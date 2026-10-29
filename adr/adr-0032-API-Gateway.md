# centralizar el acceso a las api de mycomplex mediante un api gateway

* Estado: propuesto
* Fecha: 2026-10-13

## Contexto y planteamiento del problema

MyComplex contará con una aplicación web desarrollada en Angular y un backend en Java con Spring Boot. Se requiere centralizar las solicitudes hacia las API del sistema para facilitar su enrutamiento, aplicar políticas comunes de seguridad y control de tráfico, y permitir la incorporación de nuevos servicios sin aumentar innecesariamente la complejidad de la arquitectura.

**¿Qué solución de API Gateway permite centralizar y gestionar las solicitudes de MyComplex, aprovechando tecnologías existentes y facilitando su integración y mantenimiento?**

## Impulsores de decisión

* Centralizar las solicitudes del frontend hacia el backend.
* Facilitar el enrutamiento y la administración del tráfico.
* Aprovechar una solución existente sin desarrollar un gateway desde cero.
* Mantener la compatibilidad con Java y Spring Boot.
* Permitir la aplicación de políticas comunes de seguridad.
* Facilitar la incorporación de nuevos servicios.
* Reducir el esfuerzo de implementación y mantenimiento.
* Mantener los costos de infraestructura acordes con el alcance del proyecto.
* Permitir su despliegue en Azure Container Apps.

## Opciones consideradas

1. Spring Cloud Gateway.
2. Kong Gateway.
3. NGINX.

## Resultado de la decisión

Se selecciona **Spring Cloud Gateway** como solución de API Gateway para MyComplex.

Esta alternativa permite centralizar las solicitudes del frontend y dirigirlas hacia el backend mediante rutas configurables, utilizando una solución existente del ecosistema Spring. De esta manera, no es necesario desarrollar desde cero los mecanismos de enrutamiento y filtrado de solicitudes.

Su integración con Java y Spring Boot facilita la implementación y el mantenimiento por parte del equipo, que utilizará estas mismas tecnologías para desarrollar el backend del sistema.

Spring Cloud Gateway también permite aplicar filtros y políticas comunes de control de tráfico, así como incorporar nuevas rutas si posteriormente se agregan otros servicios.

El gateway se implementará como una aplicación independiente del backend y se desplegará como un contenedor adicional en Azure Container Apps.

Las validaciones de permisos, las reglas de negocio y el aislamiento de la información entre conjuntos residenciales continuarán siendo responsabilidad del backend.

## Consecuencias

### Positivas

* Se establece un punto de entrada centralizado para las API de MyComplex.
* Se facilita la administración y modificación de las rutas.
* Se aprovechan funcionalidades existentes sin desarrollar un gateway desde cero.
* Se mantiene la coherencia tecnológica con Java y Spring Boot.
* Se permite aplicar filtros y políticas comunes de control de tráfico.
* Se facilita la incorporación de nuevos servicios.
* Se simplifica la comunicación del frontend con las API del sistema.

### Negativas

* Se incorpora un componente adicional que requiere configuración, despliegue y mantenimiento.
* Se incrementa el consumo de recursos de infraestructura.
* Las solicitudes deben atravesar un componente adicional, lo que puede aumentar su tiempo de respuesta.
* Una falla del gateway puede impedir el acceso al backend.
* Será necesario verificar la compatibilidad entre las versiones de Spring Boot y Spring Cloud.
* Se requieren pruebas adicionales para comprobar el enrutamiento, la seguridad y la disponibilidad del gateway.

## Pros y contras de las opciones

### Spring Cloud Gateway

**Pros**

* Integración con Java y Spring Boot.
* Permite configurar rutas y filtros personalizados.
* No requiere desarrollar desde cero el mecanismo de enrutamiento.
* Facilita la incorporación de nuevos servicios.
* Utiliza tecnologías conocidas por el equipo de desarrollo.
* Permite aplicar políticas de control de tráfico.

**Contras**

* Requiere mantener una aplicación Spring Boot independiente.
* Aumenta el consumo de recursos de infraestructura.
* Exige gestionar dependencias y compatibilidad de versiones.
* Introduce un componente adicional que debe supervisarse.

### Kong Gateway

**Pros**

* Solución especializada en la administración de API.
* Permite configurar rutas y políticas mediante plugins.
* Ofrece mecanismos de autenticación y control de tráfico.
* Es independiente de la tecnología utilizada en el backend.
* No requiere desarrollar un gateway desde cero.

**Contras**

* Requiere desplegar y administrar un componente adicional.
* Introduce una tecnología que el equipo debe aprender.
* Puede aumentar la complejidad operativa del proyecto.
* Algunas capacidades avanzadas requieren ediciones comerciales.

### NGINX

**Pros**

* Solución consolidada para gestionar solicitudes HTTP.
* Permite configurar proxy inverso y balanceo de carga.
* Compatible con diferentes tecnologías de backend.
* No requiere desarrollar desde cero el enrutamiento.
* Ofrece una alternativa para centralizar el acceso al sistema.

**Contras**

* Requiere configurar y mantener un componente adicional.
* Algunas políticas avanzadas de administración de API necesitan herramientas complementarias.
* Las configuraciones pueden aumentar en complejidad al incorporar nuevas rutas y reglas.
* Su enfoque principal como proxy inverso no incluye por sí solo todas las capacidades de un API Gateway especializado.

## Enlaces

