# centralizar la observabilidad en Azure Monitor y dashboards con Grafana

* Estado de decisión: Aceptado
* Estado de implementación: Implementado
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-11-04

Historia técnica: precisar la arquitectura de observabilidad implementada y la función de los dashboards con Grafana disponibles en Application Insights.

## Contexto y planteamiento del problema

MyComplex necesita diagnosticar errores, seguir solicitudes entre Spring Cloud Gateway y el backend, consultar métricas, analizar logs y reconstruir la secuencia temporal de una transacción. También debe investigar fallos de tareas, autenticación y notificaciones FCM, y conservar evidencia durable de las operaciones administrativas.

ADR-0015 seleccionó Azure Monitor y Application Insights como plataforma de monitoreo. Este ADR concreta esa decisión y evita confundir la interfaz de Grafana con el almacenamiento de la telemetría o con un stack independiente. ¿Cómo se centralizan logs, métricas y trazas en Azure y cómo se visualiza la secuencia de una solicitud sin duplicar plataformas de observabilidad?

## Impulsores de decisión

* Centralizar la telemetría del Gateway, backend y recursos Azure.
* Consultar logs desde el portal de Azure mediante Log Analytics y KQL.
* Visualizar métricas, logs y trazas mediante dashboards con Grafana integrados en Application Insights.
* Reconstruir solicitudes mediante diagnósticos de transacción de extremo a extremo y su secuencia temporal.
* Relacionar solicitudes, dependencias, excepciones y logs de una misma operación.
* Propagar `X-Correlation-Id` entre cliente, Gateway, backend y respuestas.
* Mantener auditoría durable de operaciones administrativas en PostgreSQL.
* Utilizar control de acceso de Azure para proteger dashboards y telemetría.
* Evitar operar Prometheus, Loki, Tempo, Grafana y Alloy como infraestructura independiente.
* Mantener costo, retención y volumen de ingestión acordes con la etapa del proyecto.

## Opciones consideradas

* Azure Monitor, Application Insights, Log Analytics y dashboards con Grafana integrados en Azure.
* Azure Managed Grafana como espacio de trabajo independiente.
* Stack autoadministrado con Prometheus, Grafana, Loki, Tempo y Grafana Alloy.
* Elastic Stack con Elasticsearch, Logstash y Kibana.
* Plataforma SaaS con Datadog o New Relic.
* Jaeger como solución específica para trazas distribuidas.

## Resultado de la decisión

Opción elegida: **"Azure Monitor, Application Insights, Log Analytics y dashboards con Grafana integrados en Azure"**, porque centraliza la telemetría en la plataforma Azure ya adoptada, permite consultar logs y visualizar métricas y trazas desde el portal, y evita mantener un stack de observabilidad independiente.

La solución asigna las siguientes responsabilidades:

* **Application Insights** recibe y correlaciona telemetría de aplicación, como solicitudes, dependencias, excepciones y trazas.
* **Log Analytics** almacena los datos de Application Insights y permite consultar logs mediante KQL.
* **Azure Monitor** reúne la observabilidad de aplicaciones y recursos Azure.
* **Dashboards con Grafana en Application Insights** presentan métricas, logs y trazas desde fuentes Azure. Grafana actúa como capa de visualización, no como almacén de telemetría.
* **Diagnósticos de transacción de extremo a extremo** muestra el recorrido de una operación, sus dependencias, excepciones y una lista ordenada temporalmente de la telemetría relacionada.
* **`X-Correlation-Id`** relaciona funcionalmente logs y respuestas entre el cliente, Gateway y backend, y facilita buscar una solicitud concreta.
* **PostgreSQL** conserva la auditoría durable de operaciones administrativas.

El acceso principal será desde el recurso Application Insights en el portal de Azure. Los dashboards con Grafana se utilizarán para visualizar información agregada. Para investigar un caso concreto se utilizarán Logs con consultas KQL, búsqueda de transacciones y la vista de detalles de transacción de extremo a extremo.

Esta decisión no selecciona Azure Managed Grafana como espacio de trabajo independiente. Esa alternativa se reservaría para necesidades como fuentes no Azure, plugins adicionales o funciones de Grafana que no estén disponibles en la experiencia integrada de Application Insights.

### Diferencia entre señales y auditoría

* Los **logs** registran eventos y mensajes de diagnóstico consultables en Log Analytics.
* Las **métricas** representan valores agregados y medibles, como cantidad de solicitudes, errores o latencia.
* Las **trazas distribuidas** reconstruyen el recorrido de una solicitud y sus dependencias.
* El **Correlation ID** es un identificador funcional que permite buscar y relacionar registros de una misma solicitud.
* La **auditoría** conserva evidencia durable de quién hizo qué y sobre qué recurso.

Application Insights y Log Analytics no reemplazan la auditoría en PostgreSQL. La auditoría tampoco reemplaza los logs operativos, las métricas ni las trazas.

No se documentan reglas de alerta específicas porque el repositorio local no contiene archivos que permitan verificar su configuración.

### Consecuencias positivas

* La observabilidad se consulta desde la plataforma Azure ya utilizada por MyComplex.
* Logs, métricas y trazas pueden visualizarse desde dashboards con Grafana sin operar un servidor Grafana propio.
* Log Analytics permite búsquedas detalladas mediante KQL.
* Application Insights correlaciona solicitudes, dependencias, excepciones y trazas.
* La secuencia temporal de una transacción facilita localizar el componente que originó un fallo o una demora.
* El Correlation ID permite comunicar y buscar con precisión un incidente reportado.
* La auditoría permanece en PostgreSQL con una retención independiente de la telemetría operativa.
* El control de acceso puede administrarse mediante identidades y roles de Azure.

### Consecuencias negativas

* La solución aumenta la dependencia operativa del ecosistema Azure.
* La ingestión, consultas y retención de telemetría pueden generar costos según el volumen.
* El muestreo puede impedir que todas las solicitudes aparezcan en las vistas de diagnóstico.
* Los dashboards y consultas KQL requieren mantenimiento y conocimiento específico.
* Una instrumentación o correlación incompleta puede fragmentar la secuencia de una transacción.
* Los logs deben excluir secretos, tokens y datos personales innecesarios.
* La experiencia integrada de Grafana admite fuentes Azure y tiene menos funciones que un espacio de Azure Managed Grafana independiente.
* La observabilidad ayuda a detectar y analizar fallos, pero no sustituye pruebas, seguridad ni auditoría.

## Pros y contras de las opciones

### Azure Monitor, Application Insights, Log Analytics y dashboards con Grafana

Solución administrada e integrada con el entorno Azure de MyComplex.

* Bien, porque centraliza telemetría de aplicación e infraestructura en Azure.
* Bien, porque permite consultar logs mediante KQL.
* Bien, porque ofrece dashboards con Grafana dentro de Application Insights.
* Bien, porque incluye diagnóstico de transacciones y secuencia temporal de eventos relacionados.
* Bien, porque reduce la infraestructura de observabilidad administrada por el equipo.
* Malo, porque aumenta la dependencia de Azure.
* Malo, porque el costo depende de ingestión, consultas y retención.
* Malo, porque la experiencia integrada de Grafana tiene funciones más limitadas que un espacio independiente.

### Azure Managed Grafana

Servicio administrado que proporciona un espacio de trabajo Grafana separado y admite distintas fuentes de datos.

* Bien, porque ofrece más funciones, plugins y fuentes que los dashboards integrados en Application Insights.
* Bien, porque se conecta de forma administrada con Azure Monitor.
* Malo, porque agrega otro recurso que debe configurarse, protegerse y administrar.
* Malo, porque su capacidad adicional no es necesaria para consultar exclusivamente la telemetría Azure actual.

### Stack autoadministrado con Prometheus, Grafana, Loki, Tempo y Alloy

Conjunto de componentes independientes para métricas, visualización, logs, trazas y recolección.

* Bien, porque ofrece portabilidad y control detallado sobre cada componente.
* Bien, porque puede ejecutarse fuera de Azure.
* Malo, porque exige administrar disponibilidad, almacenamiento, retención, actualizaciones y respaldos.
* Malo, porque duplica capacidades que la decisión actual centraliza en Azure Monitor y Application Insights.
* Malo, porque consume recursos adicionales y aumenta la complejidad operativa.

### Elastic Stack

Elasticsearch, Logstash y Kibana proporcionan almacenamiento, procesamiento y consulta flexible de eventos.

* Bien, porque ofrece búsqueda y análisis detallado de logs.
* Bien, porque admite diferentes fuentes y transformaciones.
* Malo, porque agrega una plataforma adicional que debe operarse y dimensionarse.
* Malo, porque requiere complementos o integraciones para cubrir completamente métricas y trazas.

### Datadog o New Relic

Plataformas SaaS que integran observabilidad de aplicaciones e infraestructura.

* Bien, porque reducen la administración de infraestructura propia.
* Bien, porque ofrecen experiencias unificadas para investigar incidentes.
* Malo, porque agregan otro proveedor y otro modelo de costos.
* Malo, porque duplican capacidades disponibles en el entorno Azure seleccionado.

### Jaeger

Solución especializada en trazas distribuidas.

* Bien, porque permite analizar recorridos y latencias entre componentes.
* Bien, porque puede integrarse con instrumentación basada en OpenTelemetry.
* Malo, porque se concentra en trazas y no centraliza por sí solo logs, métricas y auditoría.
* Malo, porque requeriría operar componentes adicionales para cubrir todo el alcance.

## Enlaces

* Decisión principal de monitoreo: [ADR-0015: Azure Monitor y Application Insights](adr-0015-monitoreo.md).
* Trazabilidad y auditoría durable: [ADR-0004](adr-0004-trazabilidad-datos.md).
* Ejecución en Azure: [ADR-0017](adr-0017-ejecucion-nube.md).
* Identidades y acceso a recursos Azure: [ADR-0024](adr-0024-identidades-administradas.md).
* Punto de entrada observable: [ADR-0032: API Gateway](adr-0032-API-Gateway.md).
* Evidencia documental del filtro de correlación: [diagrama de paquetes del API Gateway](../Diagrams/packages-api-gateway.svg).
* Evidencia documental del flujo de auditoría hacia PostgreSQL: [secuencia de auditoría](../Diagrams/sequence-audit.svg).
* Microsoft Learn: [Dashboards con Grafana en Application Insights](https://learn.microsoft.com/azure/azure-monitor/app/grafana-dashboards).
* Microsoft Learn: [Diagnóstico de transacciones de extremo a extremo](https://learn.microsoft.com/azure/azure-monitor/app/diagnostic-search).
* Microsoft Learn: [Visualizar datos de Azure Monitor con Grafana](https://learn.microsoft.com/azure/azure-monitor/visualize/visualize-grafana-overview).
