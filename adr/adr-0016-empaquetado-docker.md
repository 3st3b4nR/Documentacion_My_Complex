# empaquetar el backend para su despliegue

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

El backend de MyComplex debe poder ejecutarse de la misma forma en desarrollo, pruebas y producción. Se necesita una forma de empacar la aplicación junto con lo necesario para ejecutarla y así reducir diferencias entre los equipos de desarrollo y el entorno donde se publicará.

## Impulsores de decisión

* Ejecutar el backend de forma consistente en diferentes ambientes.
* Simplificar la publicación de nuevas versiones.
* Reducir problemas causados por diferencias de configuración entre equipos.
* Utilizar una solución ampliamente soportada por plataformas de nube.
* Evitar costos de licenciamiento en la etapa académica y de desarrollo.
* Facilitar la construcción de imágenes reproducibles que puedan ejecutarse posteriormente en diferentes proveedores o plataformas de contenedores.

## Opciones consideradas

* Docker
* Podman
* Despliegue directo del archivo JAR en una máquina virtual

## Resultado de la decisión

Opción elegida: **"Docker"**, porque permite empaquetar el backend de Spring Boot y sus requisitos de ejecución en una imagen que puede utilizarse de forma consistente en diferentes ambientes. Además, es ampliamente compatible con herramientas de desarrollo, integración continua y servicios de nube, lo que facilita su uso en MyComplex.

### Consecuencias positivas

* El backend se ejecuta de forma más consistente entre desarrollo y producción.
* Se facilita crear, probar y publicar nuevas versiones.
* Existe una amplia cantidad de herramientas y documentación disponibles.
* Se integra directamente con plataformas de administración de contenedores y registros de imágenes.
* Docker puede utilizarse sin costo para el contexto académico actual de MyComplex, de acuerdo con las condiciones vigentes de Docker Personal.

### Consecuencias negativas

* El equipo deberá aprender buenas prácticas para crear imágenes pequeñas y seguras.
* Las imágenes deberán mantenerse actualizadas cuando cambien sus dependencias.
* Una configuración incorrecta puede aumentar el tamaño de las imágenes o incluir información que no debería publicarse.

## Pros y contras de las opciones

### Docker

Herramienta ampliamente utilizada para empaquetar y ejecutar aplicaciones en contenedores.

* Bien, porque cuenta con amplia documentación y soporte en plataformas cloud.
* Bien, porque facilita repetir el mismo entorno en desarrollo, pruebas y producción.
* Es ampliamente compatible con herramientas de integración continua, registros y plataformas de ejecución de contenedores.
* Malo, porque requiere aprender a construir y mantener correctamente las imágenes.

### Podman

Herramienta compatible con el formato de contenedores utilizado por Docker y con un enfoque diferente de ejecución.

* Bien, porque puede ejecutar contenedores sin depender del servicio tradicional de Docker.
* Bien, porque utiliza estándares compatibles con otros registros y plataformas.
* Malo, porque el equipo tiene menos experiencia y existe menos material orientado al flujo definido para MyComplex.

### Despliegue directo del archivo JAR en una máquina virtual

Consiste en instalar Java y ejecutar el archivo de Spring Boot directamente en un servidor.

* Bien, porque es sencillo de entender inicialmente.
* Bien, porque evita una capa adicional de contenedores.
* Malo, porque obliga a configurar y mantener manualmente cada servidor.
* Malo, porque aumenta las diferencias entre ambientes y dificulta repetir los despliegues.

## Enlaces

* Relacionado con el ADR 08 de Java y Spring Boot.
* Relacionado con el ADR 17 de administración de contenedores.
* Relacionado con el ADR 20 de almacenamiento de imágenes de contenedor.
