# Índice de decisiones de arquitectura

**Aceptado** significa que el equipo adoptó la decisión. **Implementado** significa que el equipo ya aplicó la decisión. Este repositorio reúne la documentación arquitectónica. El código puede estar en el repositorio de la aplicación y los recursos desplegados pueden formar parte del trabajo de infraestructura de otro integrante.

| Estado de implementación | Cantidad |
| --- | ---: |
| Implementado | 29 |
| Parcialmente implementado | 2 |
| Documentado como arquitectura objetivo | 3 |
| Pendiente de despliegue | 0 |

No todos los ADR requieren PoC. Las PoC existentes son las únicas definidas por el equipo.

| ADR | Decisión | Estado de decisión | Estado de implementación |
| --- | --- | --- | --- |
| [ADR-0001](adr-0001-Data-Base.md) | seleccionar un gestor de base de datos multi-tenant | Aceptado | Implementado |
| [ADR-0002](adr-0002-operacion-offline.md) | encontrar la estrategia de operacion offline para porteria | Aceptado | Documentado como arquitectura objetivo |
| [ADR-0003](adr-0003-arquitectura-backend.md) | definir la arquitectura para el backend de mycomplex | Aceptado | Implementado |
| [ADR-0004](adr-0004-trazabilidad-datos.md) | conservar la trazabilidad de la informacion y operaciones criticas | Aceptado | Implementado |
| [ADR-0005](adr-0005-control-concurrencia.md) | implementar el control de concurrencia para operaciones criticas | Aceptado | Implementado |
| [ADR-0006](adr-0006-autenticacion.md) | implementar la autenticacion de usuarios | Aceptado | Implementado |
| [ADR-0007](adr-0007-autorizacion.md) | implementar autorizacion y control de acceso | Aceptado | Implementado |
| [ADR-0008](adr-0008-stack-backend.md) | definir el lenguaje y framework para el backend | Aceptado | Implementado |
| [ADR-0009](adr-0009-notificaciones-push.md) | implementar el servicio de notificaciones push | Aceptado | Implementado |
| [ADR-0010](adr-0010-correos-transaccionales.md) | implementar el servicio de correos transaccionales | Aceptado | Implementado |
| [ADR-0011](adr-0011-stack-frontend.md) | Definir el stack del frontend de MyComplex con Angular y TypeScript | Aceptado | Implementado |
| [ADR-0012](adr-0012-waf.md) | proteger la aplicación con OWASP Core Rule Set en un contenedor | Aceptado | Implementado |
| [ADR-0013](adr-0013-almacenamiento-imagenes.md) | almacenar las imagenes de las noticias | Aceptado | Implementado |
| [ADR-0014](adr-0014-internacionalizacion.md) | mostrar la plataforma en espanol e ingles | Aceptado | Documentado como arquitectura objetivo |
| [ADR-0015](adr-0015-monitoreo.md) | seleccionar la plataforma de monitoreo de mycomplex | Aceptado | Implementado |
| [ADR-0016](adr-0016-empaquetado-docker.md) | empaquetar el backend para su despliegue | Aceptado | Implementado |
| [ADR-0017](adr-0017-ejecucion-nube.md) | administrar la ejecucion del backend en la nube | Aceptado | Implementado |
| [ADR-0018](adr-0018-balanceo-carga.md) | distribuir las solicitudes entre varias instancias del backend | Aceptado | Implementado |
| [ADR-0019](adr-0019-gestion-secretos.md) | proteger contrasenas claves y credenciales | Aceptado | Implementado |
| [ADR-0020](adr-0020-registro-contenedores.md) | guardar y controlar las versiones del backend empaquetado | Aceptado | Implementado |
| [ADR-0021](adr-0021-postgresql-administrado.md) | alojar y proteger la base de datos postgresql | Aceptado | Implementado |
| [ADR-0022](adr-0022-automatizacion-pruebas-despliegues.md) | automatizar pruebas y despliegues | Aceptado | Implementado |
| [ADR-0023](adr-0023-acceso-servicios-internos.md) | restringir el acceso a los servicios internos | Aceptado | Implementado |
| [ADR-0024](adr-0024-identidades-administradas.md) | controlar que servicios pueden acceder a otros recursos | Aceptado | Implementado |
| [ADR-0025](adr-0025-comportamiento-reactivo.md) | definir el comportamiento reactivo de la aplicacion web | Aceptado | Implementado |
| [ADR-0026](adr-0026-comunicacion-api-rest.md) | definir los servicios que expone mycomplex | Aceptado | Implementado |
| [ADR-0027](adr-0027-integrar-pasarela-de-pagos.md) | integrar una pasarela de pagos para mycomplex | Aceptado | Parcialmente implementado |
| [ADR-0028](adr-0028-cicd.md) | automatizar la integracion y entrega continua | Aceptado | Implementado |
| [ADR-0029](adr-0029-estrategia-ramificacion.md) | definir la estrategia de ramificacion del repositorio | Aceptado | Implementado |
| [ADR-0030](adr-0030-estrategia-versionado.md) | definir la estrategia de versionado de mycomplex | Aceptado | Parcialmente implementado |
| [ADR-0031](adr-0031-pruebas-unitarias.md) | seleccionar el framework de pruebas unitarias del backend | Aceptado | Implementado |
| [ADR-0032](adr-0032-API-Gateway.md) | centralizar el acceso a las api de mycomplex mediante un api gateway | Aceptado | Implementado |
| [ADR-0033](adr-0033-arquitectura-observabilidad.md) | centralizar la observabilidad en Azure Monitor y dashboards con Grafana | Aceptado | Implementado |
| [ADR-0034](adr-0034-despliegue-frontend-angular.md) | desplegar el frontend angular en Azure Static Web Apps | Aceptado | Documentado como arquitectura objetivo |
