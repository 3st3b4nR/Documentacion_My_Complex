# Evidencia — Integración Wompi

* Fecha:
* Responsable:
* Versión de Java y Maven:
* Commit evaluado:
* Ambiente Wompi: Sandbox

| Prueba | Resultado esperado | Evidencia | Resultado |
| --- | --- | --- | --- |
| Ejecutar pruebas unitarias | Verificación de firma e idempotencia exitosas | Salida de `mvn test` | Pendiente |
| Evento válido | Respuesta 200 y una transición de negocio | Solicitud y respuesta saneadas | Pendiente |
| Contenido alterado | Respuesta 401 y ninguna transición | Solicitud y respuesta saneadas | Pendiente |
| Evento repetido | Respuesta 200 sin procesarlo dos veces | Conteo o registro de transición | Pendiente |
| Pago aprobado en Sandbox | Estado interno aprobado tras el webhook | Referencias sin datos sensibles | Pendiente |
| Pago declinado o con error | Estado interno correspondiente | Referencias sin datos sensibles | Pendiente |
| Reinicio o segunda réplica | PostgreSQL conserva la idempotencia | Registro persistido único | Pendiente |
| Revisión de código y logs | No aparecen llaves ni datos financieros | Resultado del escaneo | Pendiente |

## Conclusión

Pendiente. No marcar el ADR como validado hasta ejecutar las pruebas locales y de Sandbox, además de comprobar la idempotencia persistente.
