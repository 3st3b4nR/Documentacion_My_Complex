# definir la estrategia de ramificacion del repositorio

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

MyComplex será desarrollado de manera incremental y requiere una forma clara de organizar los cambios realizados por los integrantes del equipo. Se debe seleccionar una estrategia de ramificación que permita trabajar en nuevas funcionalidades y correcciones sin comprometer la estabilidad de la rama principal ni generar una cantidad innecesaria de ramas permanentes.

## Impulsores de decisión

* Mantener la rama principal en un estado estable (Resiliencia).
* Reducir conflictos y trabajo de sincronización entre ramas (Mantenibilidad).
* Facilitar la revisión de cambios antes de incorporarlos al código estable (Seguridad y Calidad).
* Mantener un proceso sencillo para un equipo pequeño (Operabilidad).
* Evitar procesos de ramificación que agreguen trabajo administrativo innecesario (Costo).
* Mantener trazabilidad sobre quién propuso, revisó e integró cada cambio (Cumplimiento y trazabilidad).

## Opciones consideradas

* GitHub Flow.
* GitFlow.
* Trunk-Based Development.

## Resultado de la decisión

Opción elegida: **"GitHub Flow"**, porque utiliza una rama principal estable y ramas de corta duración para nuevas funcionalidades, correcciones o cambios específicos. Esta estrategia ofrece el nivel de control que necesita MyComplex sin introducir la complejidad de mantener ramas permanentes adicionales.

El flujo esperado será crear una rama a partir de `main`, realizar el cambio, abrir un Pull Request, revisar y validar el cambio, y finalmente integrarlo nuevamente a `main`.

La estrategia de ramificación no define qué herramienta automatiza las validaciones, cómo se identifican las versiones ni qué framework se utiliza para pruebas. Estas decisiones se documentan en ADR independientes.

### Consecuencias positivas

* Se mantiene una estructura de ramas sencilla.
* La rama `main` representa el estado integrado y estable del proyecto.
* Los cambios pueden revisarse antes de integrarse.
* Se reduce el trabajo de sincronización entre ramas permanentes.
* Facilita trabajar en funcionalidades y correcciones de forma aislada.
* Se adapta bien al tamaño actual del equipo.
* Mantiene trazabilidad mediante Pull Requests.

### Consecuencias negativas

* Requiere disciplina para evitar ramas de larga duración.
* Los cambios pequeños también deben seguir el proceso acordado de integración.
* Si el proyecto necesitara mantener varias versiones activas simultáneamente, podría requerirse revisar la estrategia.

## Pros y contras de las opciones

### GitHub Flow

Estrategia basada en una rama principal y ramas de corta duración.

* Bien, porque es simple de entender y aplicar.
* Bien, porque reduce el número de ramas permanentes.
* Bien, porque facilita revisión e integración frecuente de cambios.
* Bien, porque se adapta al tamaño actual del equipo.
* Malo, porque exige mantener las ramas cortas y actualizadas.
* Malo, porque puede resultar insuficiente si en el futuro se mantienen múltiples versiones de producción en paralelo.

### GitFlow

Estrategia que utiliza ramas permanentes como `main` y `develop`, además de ramas de funcionalidad, versión y corrección.

* Bien, porque separa claramente distintas etapas del desarrollo.
* Bien, porque puede ayudar cuando se mantienen varias versiones en paralelo.
* Malo, porque genera más ramas y fusiones.
* Malo, porque aumenta el trabajo de sincronización para un equipo pequeño.
* Malo, porque introduce una complejidad superior a las necesidades actuales de MyComplex.

### Trunk-Based Development

Estrategia en la que los cambios se integran con mucha frecuencia sobre una única línea principal.

* Bien, porque favorece integraciones muy frecuentes.
* Bien, porque reduce ramas de larga duración.
* Malo, porque requiere una alta disciplina de integración y automatización.
* Malo, porque puede requerir mecanismos adicionales para ocultar funcionalidades incompletas.
* Malo, porque resulta más exigente para la etapa actual del equipo.

## Enlaces

* Relacionado con el ADR de integración y entrega continua.
* Relacionado con el ADR de estrategia de versionado.
