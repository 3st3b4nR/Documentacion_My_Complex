# guardar y controlar las versiones del backend empaquetado

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Cada vez que se genere una nueva versión del backend con Docker se producirá una imagen que debe guardarse antes de ser desplegada. Se necesita un lugar seguro donde almacenar esas imágenes, identificar sus versiones y permitir que la plataforma de ejecución descargue únicamente las versiones autorizadas.

## Impulsores de decisión

* Guardar las imágenes Docker del backend en un repositorio central.
* Mantener un historial de versiones que permita volver a una versión anterior si es necesario.
* Controlar quién puede publicar o descargar imágenes.
* Integrarse con la plataforma elegida para ejecutar los contenedores.

## Opciones consideradas

* Azure Container Registry (ACR)
* GitHub Container Registry (GHCR)
* Docker Hub

## Resultado de la decisión

Opción elegida: **"Azure Container Registry (ACR)"**, porque permite almacenar las imágenes Docker de MyComplex dentro del mismo entorno donde se ejecutará el backend, controlar el acceso mediante las identidades de Azure y facilitar que Azure Container Apps descargue las versiones autorizadas. Esto reduce configuraciones adicionales y mantiene el flujo de despliegue centralizado.

### Consecuencias positivas

* Las imágenes del backend quedan almacenadas y versionadas en un lugar central.
* Se facilita volver a una versión anterior si una actualización presenta problemas.
* El acceso puede limitarse a los servicios y personas autorizadas.
* Se integra directamente con Azure Container Apps y el proceso de despliegue.

### Consecuencias negativas

* Genera un costo adicional por almacenamiento y transferencia.
* Aumenta la dependencia de Azure.
* Será necesario limpiar imágenes antiguas para evitar almacenamiento innecesario.

## Pros y contras de las opciones

### Azure Container Registry (ACR)

Servicio de Azure para guardar imágenes de contenedores de forma privada.

* Bien, porque se integra directamente con Azure Container Apps.
* Bien, porque permite controlar el acceso mediante las identidades de Azure.
* Bien, porque mantiene las imágenes privadas dentro del mismo entorno de infraestructura.
* Malo, porque tiene un costo recurrente.
* Malo, porque depende de Azure.

### GitHub Container Registry (GHCR)

Registro de imágenes integrado con GitHub.

* Bien, porque queda cerca del repositorio de código y de GitHub Actions.
* Bien, porque facilita publicar imágenes desde los flujos de automatización.
* Malo, porque requiere configurar permisos entre GitHub y Azure para el despliegue.
* Malo, porque separa el almacenamiento de imágenes del entorno donde se ejecutarán.

### Docker Hub

Registro público y privado ampliamente utilizado para imágenes Docker.

* Bien, porque es conocido y fácil de utilizar.
* Bien, porque tiene amplia compatibilidad con herramientas de contenedores.
* Malo, porque agrega otro proveedor externo al flujo de despliegue.
* Malo, porque determinadas capacidades y límites dependen del plan contratado.

## Enlaces


* Validación principal: [PoC ADR-0020/0021/0023/0024 — Infraestructura Azure](../PoC/poc-020-024-infraestructura-azure/README.md).
* Validación relacionada: [Spike ADR-0022/0028 — CI/CD](../PoC/spike-022-028-cicd/README.md).
* Relacionado con el ADR 16 de Docker.
* Relacionado con el ADR 17 de Azure Container Apps.
* Relacionado con el ADR 23 de automatización de pruebas y despliegues.
