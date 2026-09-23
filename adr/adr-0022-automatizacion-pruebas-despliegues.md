# automatizar pruebas y despliegues

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Publicar nuevas versiones de MyComplex de forma manual puede provocar errores y dificultar saber qué versión fue probada y desplegada. Se necesita un proceso sencillo que permita ejecutar pruebas, construir la imagen Docker y publicar nuevas versiones de manera repetible, sin obligar al equipo a mantener un servidor adicional.

## Impulsores de decisión

* Reducir errores humanos durante la publicación de nuevas versiones.
* Ejecutar pruebas automáticamente antes de desplegar cambios.
* Mantener un registro de los procesos de construcción y despliegue.
* Integrarse con GitHub, Docker y los servicios seleccionados para MyComplex.
* Mantener costos bajos durante la etapa académica y de validación.
* Evitar administrar infraestructura adicional únicamente para automatizar despliegues.

## Opciones consideradas

* GitHub Actions.
* Azure DevOps Pipelines.
* Jenkins administrado por el equipo.
* Mantener despliegues completamente manuales.

## Resultado de la decisión

Opción elegida: **"GitHub Actions"**, porque permite automatizar las pruebas, la construcción de la imagen Docker y el despliegue desde el mismo repositorio donde se mantiene el código. Para el tamaño actual de MyComplex ofrece una solución sencilla, con una capa gratuita suficiente para la etapa inicial y sin necesidad de instalar o mantener un servidor adicional.

### Consecuencias positivas

* Las pruebas pueden ejecutarse automáticamente antes de publicar una nueva versión.
* Se reducen los pasos manuales durante los despliegues.
* Queda un registro de las ejecuciones realizadas.
* Se integra directamente con GitHub y Docker.
* No requiere administrar un servidor propio para la automatización.

### Consecuencias negativas

* Los archivos de automatización deben mantenerse junto con el proyecto.
* Una configuración incorrecta de permisos podría permitir acciones no deseadas.
* Si el proyecto supera los límites gratuitos del plan utilizado, podrían aparecer costos adicionales.
* El proceso queda parcialmente ligado a GitHub.

## Pros y contras de las opciones

### GitHub Actions

Herramienta de automatización integrada directamente con los repositorios de GitHub.

* Bien, porque mantiene el código y la automatización en la misma plataforma.
* Bien, porque se integra fácilmente con Java, Maven y Docker.
* Bien, porque puede utilizarse sin costo adicional dentro de los límites gratuitos disponibles para la etapa actual.
* Bien, porque no requiere administrar servidores propios.
* Malo, porque genera dependencia de GitHub para la automatización.

### Azure DevOps Pipelines

Herramienta de Microsoft para construir, probar y desplegar aplicaciones.

* Bien, porque se integra de forma natural con servicios Azure.
* Bien, porque ofrece funciones avanzadas para proyectos grandes.
* Malo, porque agregaría otra plataforma diferente al repositorio de GitHub.
* Malo, porque aumenta la cantidad de herramientas que el equipo debe aprender y administrar.

### Jenkins administrado por el equipo

Servidor de automatización que debe instalarse, actualizarse y protegerse directamente.

* Bien, porque ofrece mucha flexibilidad.
* Bien, porque puede adaptarse a distintos entornos.
* Malo, porque requiere mantener un servidor adicional.
* Malo, porque añade trabajo operativo innecesario para el tamaño actual del proyecto.

### Mantener despliegues completamente manuales

El equipo ejecuta las pruebas, construye la imagen y publica cada versión manualmente.

* Bien, porque no requiere configurar una herramienta nueva.
* Malo, porque aumenta el riesgo de errores humanos.
* Malo, porque hace más difícil repetir exactamente el mismo proceso en cada despliegue.

## Enlaces

* Relacionado con el ADR 16 de contenedores.
* Relacionado con el ADR 20 de almacenamiento de imágenes de contenedor.
* Relacionado con el ADR 17 de administración de contenedores.
