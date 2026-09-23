# seleccionar el framework de pruebas unitarias del backend

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

El backend de MyComplex contiene reglas de negocio relacionadas con usuarios, permisos, visitantes, reservas, pagos y aislamiento de información entre conjuntos residenciales. Se necesita seleccionar un framework de pruebas unitarias que permita validar estas reglas de manera repetible y detectar errores antes de que los cambios sean integrados o publicados.

## Impulsores de decisión

* Detectar errores en reglas de negocio antes de que afecten a los usuarios (Resiliencia).
* Facilitar la modificación del código manteniendo confianza sobre su comportamiento (Mantenibilidad).
* Integrarse de forma natural con Java y Spring Boot (Operabilidad).
* Permitir ejecución automática sin necesidad de infraestructura adicional (Costo).
* Facilitar evidencia repetible de que determinadas reglas funcionan como se espera (Cumplimiento y trazabilidad).
* Favorecer pruebas de validaciones relacionadas con permisos, aislamiento y operaciones sensibles (Seguridad).

## Opciones consideradas

* JUnit 5.
* TestNG.
* Spock.

## Resultado de la decisión

Opción elegida: **"JUnit 5"**, porque es el framework de pruebas más alineado con el ecosistema Java utilizado por MyComplex, cuenta con soporte maduro y permite estructurar pruebas unitarias de manera clara sin introducir un lenguaje adicional ni una herramienta especializada innecesaria.

Las pruebas unitarias se utilizarán principalmente para validar lógica de negocio, reglas de permisos, validaciones, estados y comportamientos esperados de los servicios del backend.

Este ADR únicamente selecciona el framework principal para definir y ejecutar pruebas unitarias. No define herramientas auxiliares de mocking, estrategias de integración continua ni tipos de pruebas adicionales.

### Consecuencias positivas

* Permite validar automáticamente reglas de negocio críticas.
* Se integra directamente con el ecosistema Java y Spring Boot.
* Facilita ejecutar las pruebas de manera repetible.
* Reduce el riesgo de introducir regresiones durante modificaciones.
* Tiene una curva de aprendizaje baja para desarrolladores Java.
* No requiere introducir un lenguaje diferente al utilizado por el backend.

### Consecuencias negativas

* Las pruebas deben mantenerse cuando cambien las reglas de negocio.
* Una cobertura alta no garantiza por sí sola que el sistema esté libre de errores.
* Las pruebas unitarias no reemplazan pruebas de integración, seguridad, rendimiento o aceptación.

## Pros y contras de las opciones

### JUnit 5

Framework ampliamente utilizado para pruebas en aplicaciones Java.

* Bien, porque está alineado con Java y Spring Boot.
* Bien, porque posee amplia documentación y adopción.
* Bien, porque facilita estructurar y ejecutar pruebas unitarias.
* Bien, porque puede ejecutarse sin infraestructura adicional.
* Malo, porque requiere disciplina para mantener pruebas útiles y legibles.

### TestNG

Framework de pruebas para Java con capacidades avanzadas de configuración y agrupación.

* Bien, porque permite configuraciones flexibles de suites y grupos de pruebas.
* Bien, porque ofrece mecanismos avanzados de parametrización.
* Malo, porque agrega una alternativa adicional sin una ventaja clara para las necesidades actuales de MyComplex.
* Malo, porque el equipo tendría que mantener una herramienta menos alineada con el flujo habitual de Spring Boot.

### Spock

Framework de pruebas expresivo que se ejecuta sobre la JVM.

* Bien, porque permite escribir pruebas muy legibles y expresivas.
* Bien, porque ofrece capacidades potentes para especificaciones y pruebas de comportamiento.
* Malo, porque introduce Groovy como lenguaje adicional.
* Malo, porque aumenta la curva de aprendizaje y la cantidad de tecnologías del proyecto.
* Malo, porque esa complejidad no se justifica para el alcance actual.

## Enlaces

* Relacionado con el ADR de Java y Spring Boot.
* Relacionado con el ADR de integración y entrega continua.
