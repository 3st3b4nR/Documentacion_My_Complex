# definir la arquitectura de observabilidad de mycomplex

* Estado de decisión: Aceptado
* Estado de implementación: Implementado
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-11-03

Historia técnica: documentar la instrumentación y el stack de observabilidad implementados, complementando la decisión de plataforma administrada de ADR-0015.

## Contexto y planteamiento del problema

MyComplex necesita diagnosticar errores y seguir solicitudes entre el cliente, Spring Cloud Gateway y el backend Spring Boot. También debe consultar métricas técnicas y funcionales, analizar logs estructurados, seguir trazas distribuidas, investigar fallos de tareas, autenticación y notificaciones FCM, y conservar evidencia durable de las operaciones administrativas.

El equipo confirmó la implementación de un stack abierto que puede ejecutarse localmente y trasladarse entre ambientes. La decisión debe distinguir la telemetría operativa de la auditoría persistente y explicar cómo esta solución convive con Azure Monitor y Application Insights, seleccionados en ADR-0015. ¿Qué arquitectura permite correlacionar logs, métricas, trazas y auditoría con bajo acoplamiento a un proveedor?

Este repositorio de documentación no contiene los archivos Compose, dashboards, propiedades ni pruebas de la aplicación. Por esa razón, este ADR no inventa rutas hacia esos artefactos. La evidencia local disponible se limita a los diagramas y ADR enlazados al final.

## Impulsores de decisión

* Utilizar estándares abiertos que permitan exportar telemetría a diferentes plataformas.
* Reproducir localmente la observabilidad del Gateway y el backend.
* Relacionar métricas, logs y trazas para investigar un mismo incidente.
* Mantener compatibilidad con Spring Boot, Spring Cloud Gateway y Micrometer.
* Propagar `X-Correlation-Id` entre cliente, Gateway, backend y respuestas.
* Conservar auditoría durable y consultable en PostgreSQL.
* Reducir la dependencia de un proveedor sin impedir la integración con Azure.
* Mantener costo y consumo operativo acordes con la etapa académica y el tamaño actual del sistema.
* Evitar registrar secretos, tokens o datos personales innecesarios.

## Opciones consideradas

* Stack abierto con OpenTelemetry, Micrometer, Prometheus, Grafana, Loki, Tempo y Grafana Alloy.
* Azure Monitor y Application Insights como solución principal y exclusiva.
* Elastic Stack con Elasticsearch, Logstash y Kibana.
* Plataforma SaaS de observabilidad con Datadog o New Relic.
* Jaeger como solución específica para trazas distribuidas.

## Resultado de la decisión

Opción elegida: **"Stack abierto con OpenTelemetry, Micrometer, Prometheus, Grafana, Loki, Tempo y Grafana Alloy"**, porque integra los tres tipos principales de telemetría, puede reproducirse localmente, es compatible con el ecosistema Spring y mantiene abierta la posibilidad de exportar datos a una plataforma administrada.

La arquitectura implementada asigna una responsabilidad concreta a cada pieza:

* **OpenTelemetry** instrumenta y propaga el contexto de las trazas con un estándar neutral respecto al proveedor.
* **Micrometer** expone métricas del backend y del Gateway mediante las convenciones del ecosistema Spring.
* **Prometheus** recolecta y almacena series temporales de métricas.
* **Grafana** consulta las fuentes de datos y presenta dashboards de métricas, logs y trazas.
* **Loki** almacena y permite consultar logs estructurados.
* **Tempo** almacena las trazas distribuidas.
* **Grafana Alloy** recolecta y envía logs hacia Loki.
* Los servicios generan **logs ECS JSON**, adecuados para consulta estructurada.
* El encabezado **`X-Correlation-Id`** se propaga entre cliente, Gateway y backend para relacionar registros y devolver un identificador de diagnóstico al consumidor.
* La **auditoría durable** registra en PostgreSQL quién realizó una operación administrativa, qué acción ejecutó y sobre qué recurso.

Los conceptos no son intercambiables. Los **logs** describen eventos para diagnóstico. Las **métricas** representan valores agregados y medibles. Las **trazas** reconstruyen el recorrido de una solicitud entre componentes. El **Correlation ID** permite unir funcionalmente logs y respuestas, pero no sustituye el contexto de una traza. La **auditoría** conserva evidencia durable de acciones relevantes. Loki no reemplaza esa evidencia y la auditoría en PostgreSQL no reemplaza los logs operativos.

Azure Monitor y Application Insights conservan la decisión de ADR-0015. Pueden complementar el stack abierto como capa administrada y centralizada para el entorno Azure. OpenTelemetry reduce el acoplamiento y permite exportar telemetría posteriormente, pero este ADR no afirma que Application Insights sustituya automáticamente Prometheus, Loki, Tempo o Grafana.

No se documentan reglas de alerta concretas porque este repositorio no contiene archivos locales que permitan verificar su definición.

### Consecuencias positivas

* Métricas, logs y trazas pueden investigarse de forma conjunta.
* El entorno local puede reproducir la arquitectura de observabilidad sin depender exclusivamente de Azure.
* OpenTelemetry y los formatos abiertos facilitan migrar o exportar telemetría a otra plataforma.
* Micrometer ofrece integración natural con Spring Boot y Spring Cloud Gateway.
* El Correlation ID facilita comunicar y localizar errores reportados por clientes.
* La auditoría en PostgreSQL conserva evidencia aunque roten o expiren los logs operativos.
* La solución puede complementarse con Azure Monitor y Application Insights sin invalidar ADR-0015.

### Consecuencias negativas

* El stack requiere más operación que una solución SaaS completamente administrada.
* El equipo debe administrar almacenamiento, retención, respaldos y disponibilidad de cada componente.
* Etiquetas de alta cardinalidad pueden aumentar de forma importante el consumo de métricas y logs.
* Prometheus, Loki, Tempo, Grafana y Alloy consumen recursos adicionales.
* Los endpoints de métricas, dashboards y datos de telemetría deben protegerse.
* Los logs estructurados deben excluir credenciales, tokens y datos personales innecesarios.
* La presencia de dashboards locales no demuestra por sí sola una operación productiva completa.
* La observabilidad ayuda a detectar y analizar fallos, pero no sustituye pruebas, controles de seguridad ni auditoría.

## Pros y contras de las opciones

### Stack abierto con OpenTelemetry, Micrometer, Prometheus, Grafana, Loki, Tempo y Alloy

Solución portable que integra instrumentación, métricas, logs, trazas y visualización.

* Bien, porque utiliza estándares y protocolos con bajo acoplamiento a un proveedor.
* Bien, porque puede ejecutarse de forma local y reproducible.
* Bien, porque integra fuentes de datos diferentes en Grafana.
* Bien, porque es compatible con Spring Boot y Spring Cloud Gateway.
* Bien, porque permite exportar telemetría hacia servicios administrados posteriormente.
* Malo, porque exige operar, actualizar y proteger varios componentes.
* Malo, porque el almacenamiento y la retención quedan bajo responsabilidad del equipo.
* Malo, porque una configuración deficiente de etiquetas puede elevar la cardinalidad y el consumo.

### Azure Monitor y Application Insights

Plataforma administrada ya seleccionada en ADR-0015 para centralizar monitoreo en Azure.

* Bien, porque reduce la administración de infraestructura de observabilidad.
* Bien, porque se integra con los recursos desplegados en Azure.
* Bien, porque puede centralizar telemetría y alertas del entorno administrado.
* Malo, porque aumenta el acoplamiento operativo con Azure si se adopta como única salida.
* Malo, porque la ingestión y retención crecen con el volumen de telemetría.
* Malo, porque no reproduce por sí sola el entorno local utilizado por el equipo.

### Elastic Stack

Elasticsearch, Logstash y Kibana proporcionan almacenamiento, procesamiento y consulta flexible de eventos.

* Bien, porque ofrece búsqueda potente y visualización de logs.
* Bien, porque admite diferentes fuentes y transformaciones.
* Malo, porque su operación y ajuste de recursos agregan complejidad para el tamaño actual del proyecto.
* Malo, porque requiere complementar o integrar capacidades específicas de métricas y trazas.

### Datadog o New Relic

Plataformas SaaS que integran observabilidad y reducen la operación de infraestructura propia.

* Bien, porque ofrecen una experiencia administrada y unificada.
* Bien, porque reducen el esfuerzo de mantener almacenamiento y visualización.
* Malo, porque introducen otro proveedor y un costo que depende del volumen y uso.
* Malo, porque aportan menos reproducibilidad local que el stack actual.

### Jaeger

Solución especializada en trazas distribuidas.

* Bien, porque permite visualizar recorridos y latencias entre servicios.
* Bien, porque se integra con instrumentación basada en OpenTelemetry.
* Malo, porque resuelve principalmente trazas y no unifica por sí solo métricas, logs y auditoría.
* Malo, porque requeriría componentes adicionales para cubrir el alcance completo.

## Enlaces

* Decisión de plataforma administrada complementaria: [ADR-0015: Azure Monitor y Application Insights](adr-0015-monitoreo.md).
* Trazabilidad y auditoría durable: [ADR-0004](adr-0004-trazabilidad-datos.md).
* Ejecución en Azure: [ADR-0017](adr-0017-ejecucion-nube.md).
* Automatización de pruebas y despliegues: [ADR-0022](adr-0022-automatizacion-pruebas-despliegues.md) y [ADR-0028](adr-0028-cicd.md).
* Punto de entrada observable: [ADR-0032: API Gateway](adr-0032-API-Gateway.md).
* Evidencia documental del filtro de correlación: [diagrama de paquetes del API Gateway](../Diagrams/packages-api-gateway.svg).
* Evidencia documental del flujo de auditoría hacia PostgreSQL: [secuencia de auditoría](../Diagrams/sequence-audit.svg).
* Vista de monitoreo documentada: [C4 nivel 2](../Diagrams/c4-level-2-containers.svg).
