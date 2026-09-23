# definir la estrategia de versionado de mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

MyComplex necesita identificar de forma clara las versiones que se publiquen a medida que el sistema evolucione. Se debe seleccionar una estrategia de versionado que permita distinguir nuevas funcionalidades, correcciones y cambios incompatibles, facilitando la trazabilidad entre el código, los artefactos generados y las versiones desplegadas.

## Impulsores de decisión

* Identificar claramente el impacto de cada nueva versión (Mantenibilidad).
* Facilitar la trazabilidad entre código, artefactos y despliegues (Cumplimiento y Operabilidad).
* Permitir reconocer rápidamente si una versión incorpora funcionalidades, correcciones o cambios incompatibles (Operabilidad).
* Mantener un esquema comprensible para desarrolladores y responsables de despliegue (Mantenibilidad).
* Evitar un mecanismo de versionado que requiera herramientas costosas o procesos complejos (Costo).
* Facilitar la recuperación o comparación con versiones anteriores cuando ocurra un problema (Resiliencia).

## Opciones consideradas

* Versionado Semántico (SemVer).
* Versionado basado en calendario (CalVer).
* Versionado secuencial.

## Resultado de la decisión

Opción elegida: **"Versionado Semántico (SemVer)"**, porque permite expresar el tipo de cambio realizado mediante una estructura clara `MAJOR.MINOR.PATCH`.

MyComplex utilizará versiones con el formato:

`MAJOR.MINOR.PATCH`

* `MAJOR`: cambios incompatibles con versiones anteriores.
* `MINOR`: nuevas funcionalidades compatibles.
* `PATCH`: correcciones compatibles que no agregan una funcionalidad nueva.

Ejemplos:

* `1.0.0`: primera versión estable.
* `1.1.0`: incorporación de una nueva funcionalidad compatible.
* `1.1.1`: corrección de un error.
* `2.0.0`: cambio incompatible que requiere adaptación.

Las versiones publicadas podrán representarse mediante etiquetas de Git como `v1.1.0`.

Este ADR únicamente define cómo se identifican las versiones. No define la estrategia de ramas, el mecanismo de automatización ni la herramienta utilizada para generar artefactos.

### Consecuencias positivas

* Las versiones comunican de forma clara el impacto de los cambios.
* Facilita la trazabilidad entre código y publicaciones.
* Permite identificar rápidamente correcciones y nuevas funcionalidades.
* Es un esquema ampliamente conocido y sencillo de documentar.
* Facilita relacionar artefactos de despliegue con una versión específica.
* Ayuda a determinar qué versión estaba en ejecución cuando ocurre un incidente.

### Consecuencias negativas

* El equipo debe clasificar correctamente el tipo de cambio realizado.
* Puede existir discusión sobre cuándo un cambio debe considerarse incompatible.
* Requiere disciplina para evitar versiones inconsistentes con los cambios reales.

## Pros y contras de las opciones

### Versionado Semántico (SemVer)

Sistema que representa las versiones mediante `MAJOR.MINOR.PATCH`.

* Bien, porque comunica el impacto técnico de cada versión.
* Bien, porque es fácil de comprender.
* Bien, porque facilita identificar correcciones, nuevas funcionalidades y cambios incompatibles.
* Bien, porque se integra naturalmente con etiquetas de repositorio y artefactos.
* Malo, porque requiere clasificar correctamente los cambios.
* Malo, porque depende de disciplina para mantener coherencia.

### Versionado basado en calendario (CalVer)

Sistema que identifica versiones mediante la fecha o periodo de publicación.

* Bien, porque permite conocer rápidamente cuándo se publicó una versión.
* Bien, porque resulta útil para productos con ciclos de publicación ligados al tiempo.
* Malo, porque no comunica directamente si una versión introduce incompatibilidades.
* Malo, porque el calendario no representa por sí mismo el impacto funcional del cambio.

### Versionado secuencial

Sistema que asigna números consecutivos a cada publicación.

* Bien, porque es muy sencillo de aplicar.
* Bien, porque evita discusiones sobre clasificación de cambios.
* Malo, porque un número secuencial no explica el impacto de la versión.
* Malo, porque dificulta distinguir una corrección de una nueva funcionalidad o un cambio incompatible.

## Enlaces

* Relacionado con el ADR de estrategia de ramificación.
* Relacionado con el ADR de integración y entrega continua.
* Relacionado con el ADR de registro de imágenes de contenedor.
