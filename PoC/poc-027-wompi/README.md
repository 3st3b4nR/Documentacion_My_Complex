# PoC ADR-0027 — Integración Wompi

## Objetivo

Validar el punto más sensible de la integración: recepción de eventos HTTPS `POST`, verificación SHA-256 del checksum y procesamiento idempotente. La aplicación no confía en el retorno del navegador para aprobar un pago.

## Dependencias

* ADR-0019: el secreto de eventos y las llaves privadas deben residir en Key Vault.
* ADR-0026: Wompi y MyComplex se comunican mediante HTTPS y JSON.

## Alcance implementado

* Endpoint `POST /webhooks/wompi`.
* Validación del checksum del header y del cuerpo según las propiedades declaradas por Wompi.
* Rechazo de contenido alterado.
* Tratamiento idempotente de un evento repetido.
* Pruebas unitarias sin credenciales ni dinero real.
* Lista de evidencia en `docs/evidence-checklist.md`.

El almacenamiento de la PoC es deliberadamente en memoria. La implementación real debe persistir el identificador/checksum del evento y el estado de la transacción en PostgreSQL dentro de una transacción; de lo contrario, la idempotencia se pierde al reiniciar o al ejecutar varias réplicas.

## Ejecución local

```powershell
mvn test
```

Se requiere JDK 21 y Maven. Para iniciar el endpoint defina `WOMPI_EVENT_SECRET` únicamente en el entorno y ejecute `mvn spring-boot:run`.

## Validación sandbox pendiente

1. Obtener llaves `pub_test_`/`prv_test_` y secreto de eventos del ambiente Sandbox.
2. Publicar temporalmente el endpoint mediante HTTPS.
3. Configurar una URL de eventos separada de producción.
4. Probar transacciones aprobada, declinada y con error.
5. Repetir el mismo evento y comprobar una sola transición de negocio.
6. Alterar monto/estado conservando el checksum y comprobar respuesta 401.
7. Guardar evidencia saneada sin llaves, secretos, datos financieros o personales.

## Estado

La lógica y las pruebas automatizadas quedan preparadas, pero no se ejecutaron en la estación donde se generó la PoC porque solo dispone de Java 8 y no tiene Maven. La PoC permanece pendiente hasta ejecutar `mvn test` con JDK 21, completar el flujo Sandbox y sustituir la idempotencia en memoria por persistencia PostgreSQL.

## Fuentes

* [Eventos y validación de checksum](https://docs.wompi.co/docs/colombia/eventos/)
* [Ambientes y llaves](https://docs.wompi.co/docs/colombia/ambientes-y-llaves/)
* [Datos de prueba en Sandbox](https://docs.wompi.co/docs/colombia/datos-de-prueba-en-sandbox/)
