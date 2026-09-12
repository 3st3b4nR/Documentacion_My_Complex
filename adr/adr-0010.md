# implementar el servicio de correos transaccionales

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-11

## Contexto y planteamiento del problema

MyComplex necesita enviar comunicaciones formales por correo electrónico, tales como credenciales de acceso, recordatorios de asambleas, reportes de cartera y confirmaciones de reservas. Configurar y mantener un servidor SMTP propio es complejo y conlleva un alto riesgo de que los correos sean marcados como *spam*. ¿Qué proveedor de correo transaccional (Email API) ofrece la mejor experiencia de desarrollo y una capa gratuita adecuada para la etapa actual del sistema?

## Impulsores de decisión

* Asegurar una alta tasa de entregabilidad (evitar la carpeta de *spam*).
* Disponer de una capa gratuita generosa para desarrollo y pruebas.
* Facilitar el diseño e inyección de datos dinámicos en las plantillas de correo.
* Evitar bloqueos técnicos por políticas excesivamente estrictas en la creación de cuentas nuevas.

## Opciones consideradas

* Servidor SMTP propio.
* SendGrid (Twilio).
* Resend.

## Resultado de la decisión

Opción elegida: **Resend**, porque es una plataforma moderna orientada a la experiencia del desarrollador. Ofrece una capa gratuita sólida (3,000 correos mensuales) que cubre holgadamente las necesidades de validación del proyecto. Además, permite un registro ágil sin las fricciones comunes de bloqueo automático que presentan otras plataformas antiguas en cuentas nuevas. Su API es limpia, facilitando la integración rápida con el backend para enviar plantillas dinámicas.

### Consecuencias positivas

* Integración extremadamente rápida y documentación clara.
* Permite inyectar variables dinámicas en los correos con un esfuerzo mínimo.
* No se invierte tiempo en configuraciones complejas de servidores de correo.

### Consecuencias negativas

* Al ser una herramienta más reciente en el mercado, existe menos material en foros de terceros frente a opciones con décadas de antigüedad.

## Pros y contras de las opciones

### Servidor SMTP propio
* Bien, porque no hay límites artificiales de envío de terceros.
* Malo, porque los correos originados en servidores no reconocidos casi siempre son marcados como *spam* por Gmail y Outlook.
* Malo, porque exige configurar registros DNS (SPF, DKIM, DMARC) manualmente y mantener la reputación de la IP.

### SendGrid (Twilio)
* Bien, porque es un estándar maduro utilizado por grandes corporaciones.
* Malo, porque sus filtros de prevención de *spam* suelen bloquear o suspender cuentas gratuitas recién creadas, retrasando el avance del desarrollo.

### Resend
* Bien, porque ofrece la experiencia de desarrollo más moderna y ágil del mercado actual.
* Bien, porque su cuota gratuita es amplia y su API es muy sencilla de consumir.
* Malo, porque su ecosistema de integraciones de terceros aún está en crecimiento en comparación con los gigantes de la industria.