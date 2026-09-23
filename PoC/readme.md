# Catálogo de validaciones para ADR-0020 a ADR-0030

Este directorio agrupa las PoC y spikes que aportan evidencia técnica a las decisiones comprendidas entre ADR-0020 y ADR-0030. No se crea una PoC por cada ADR: las decisiones que comparten una misma cadena técnica se validan juntas.

| Artefacto | Tipo | ADR principales | ADR dependientes | Estado |
| --- | --- | --- | --- | --- |
| `poc-020-024-infraestructura-azure` | PoC integrada | 0020, 0021, 0023, 0024 | 0013, 0016, 0017, 0019 | Preparada; ejecución pendiente de suscripción Azure |
| `spike-022-028-cicd` | Spike con workflow ejecutable | 0022, 0028 | 0016, 0020, 0029, 0030 | Preparado; activación pendiente del repositorio de aplicación |
| `poc-027-wompi` | PoC de código | 0027 | 0019, 0026 | Código y pruebas preparados; ejecución pendiente de JDK 21, Maven y Sandbox |

## ADR sin PoC independiente

* **ADR-0025:** se valida en la sección vertical del frontend y en la PoC offline del ADR-0002. El riesgo es el manejo de estados de interfaz, no la viabilidad de una tecnología adicional.
* **ADR-0026:** REST/JSON se valida en las integraciones de autorización, frontend y Wompi. Debe complementarse con pruebas de contrato cuando exista la API principal.
* **ADR-0029:** GitHub Flow es una práctica de proceso. Se valida mediante ramas cortas, revisión y protección de `main`, no mediante código experimental.
* **ADR-0030:** SemVer es una convención. El spike de CI/CD incluye una comprobación automática del formato de las etiquetas.

## Regla de evidencia

Una validación se considera concluida únicamente cuando su checklist contiene fecha, versiones, salida verificable, resultado esperado y resultado obtenido. Los archivos preparados sin acceso al proveedor se consideran diseño ejecutable, no evidencia de éxito.
