# seleccionar la plataforma de monitoreo de mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

Cuando MyComplex esté en funcionamiento será necesario conocer si la plataforma presenta errores, tiempos de respuesta anormales o fallos en sus servicios. Se debe seleccionar una plataforma de monitoreo que permita centralizar y consultar esta información sin obligar al equipo a mantener infraestructura propia y sin generar un costo fijo elevado durante la etapa inicial del proyecto.

## Impulsores de decisión

* Detectar errores y problemas de rendimiento con rapidez (Operabilidad y Resiliencia).
* Facilitar la investigación de incidentes mediante información centralizada (Operabilidad).
* Evitar mantener servidores propios destinados únicamente al monitoreo (Mantenibilidad).
* Mantener un costo inicial bajo y permitir pagar según el crecimiento del uso (Eficiencia de costo).
* Integrarse adecuadamente con la infraestructura donde se ejecutará MyComplex (Operabilidad).
* Permitir controlar qué información se registra para evitar almacenar datos personales innecesarios (Seguridad y Cumplimiento).
* Permitir ampliar la capacidad de monitoreo cuando aumente el tráfico o la criticidad del sistema (Mantenibilidad).

## Opciones consideradas

* Azure Monitor Application Insights.
* Datadog.
* New Relic.

## Resultado de la decisión

Opción elegida: **"Azure Monitor Application Insights"**, porque permite centralizar información sobre errores, solicitudes, dependencias y tiempos de respuesta sin necesidad de administrar servidores de monitoreo propios. Además, se integra de forma natural con los servicios de Azure previstos para MyComplex y permite comenzar con una cobertura gratuita de ingestión de datos antes de generar costos por consumo adicional.

Actualmente, el nivel de pago por uso de Log Analytics asociado a Application Insights incluye una asignación gratuita de los primeros 5 GB de datos ingeridos por mes y por cuenta de facturación. Esto permite que la etapa académica, de pruebas y de bajo tráfico pueda operar inicialmente sin un costo relevante de monitoreo, siempre que el volumen generado permanezca dentro de dicha cobertura.

La plataforma elegida será responsable de recibir, almacenar, consultar y visualizar la información de monitoreo. La forma en que la aplicación genera o instrumenta esa telemetría no se decide en este ADR y deberá documentarse por separado para evitar combinar decisiones independientes.

### Consecuencias positivas

* El equipo puede consultar errores, solicitudes y tiempos de respuesta desde una plataforma centralizada.
* No se requiere instalar ni mantener servidores propios de monitoreo.
* La cobertura gratuita inicial permite validar la solución con bajo costo.
* El costo puede crecer de acuerdo con el volumen real de información ingerida.
* Se integra con otros servicios Azure utilizados por MyComplex.
* Permite crear paneles y alertas para apoyar la operación del sistema.
* Facilita investigar incidentes y detectar comportamientos anormales.

### Consecuencias negativas

* Si el volumen de telemetría supera la cobertura gratuita, se generarán cargos por ingestión y retención.
* Será necesario controlar qué información se envía para evitar costos innecesarios.
* Una configuración incorrecta podría registrar datos personales o sensibles.
* El equipo dependerá de Azure para consultar la información centralizada.
* Algunas capacidades avanzadas pueden aumentar el costo operativo.

## Pros y contras de las opciones

### Azure Monitor Application Insights

Plataforma de observabilidad de Azure para supervisar aplicaciones y servicios.

* Bien, porque no requiere administrar infraestructura propia de monitoreo.
* Bien, porque se integra con la infraestructura Azure prevista para MyComplex.
* Bien, porque dispone de una asignación gratuita inicial de ingestión que favorece la etapa académica y de bajo tráfico.
* Bien, porque permite consultar errores, solicitudes, dependencias y tiempos de respuesta desde un mismo entorno.
* Bien, porque puede escalar según aumente el uso.
* Malo, porque un volumen elevado de telemetría puede aumentar los costos.
* Malo, porque aumenta la dependencia del ecosistema Azure.

### Datadog

Plataforma externa especializada en observabilidad y monitoreo.

* Bien, porque ofrece herramientas avanzadas para aplicaciones e infraestructura.
* Bien, porque permite centralizar métricas, registros y trazas.
* Malo, porque introduce otro proveedor que el equipo debe administrar.
* Malo, porque sus capacidades pueden resultar sobredimensionadas para la etapa actual.
* Malo, porque el costo puede aumentar al crecer el volumen de datos y recursos supervisados.

### New Relic

Plataforma externa de observabilidad para aplicaciones e infraestructura.

* Bien, porque ofrece monitoreo de rendimiento, errores y servicios desde una plataforma centralizada.
* Bien, porque puede utilizarse independientemente del proveedor de nube.
* Malo, porque agrega un proveedor adicional al proyecto.
* Malo, porque implica aprender y administrar otra plataforma.
* Malo, porque las necesidades actuales de MyComplex pueden cubrirse con una alternativa ya integrada al entorno de despliegue seleccionado.

## Enlaces

* Relacionado con el ADR de Java y Spring Boot.
* Relacionado con el ADR de administración de contenedores.
* Relacionado con el ADR de protección de la aplicación.
* La estrategia utilizada para generar e instrumentar la telemetría deberá documentarse en un ADR independiente.
* Azure Monitor pricing: https://azure.microsoft.com/pricing/details/monitor/
* Application Insights FAQ: https://learn.microsoft.com/azure/azure-monitor/app/application-insights-faq
