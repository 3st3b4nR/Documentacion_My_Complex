# integrar una pasarela de pagos para mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-23

## Contexto y planteamiento del problema

MyComplex incorporará pagos en línea para permitir que los usuarios realicen obligaciones económicas relacionadas con el conjunto residencial, inicialmente el pago de la administración y otros conceptos que posteriormente sean aprobados dentro del alcance funcional del sistema. Se necesita seleccionar una pasarela de pagos que permita integrar estos cobros de forma segura, con medios de pago usados en Colombia, costos de transacción razonables y mecanismos confiables para conocer el estado real de cada pago.

## Impulsores de decisión

* Mantener un costo por transacción razonable para no afectar significativamente el costo operativo de MyComplex.
* Permitir medios de pago ampliamente utilizados en Colombia, especialmente PSE, tarjetas y billeteras digitales.
* Evitar que MyComplex almacene directamente datos sensibles de tarjetas o credenciales financieras.
* Contar con un ambiente de pruebas que permita validar la integración antes de utilizar dinero real.
* Recibir confirmaciones automáticas del estado de los pagos mediante eventos o webhooks.
* Facilitar la integración con el backend de MyComplex desarrollado en Java y Spring Boot.
* Mantener trazabilidad sobre pagos aprobados, rechazados o pendientes.
* Evitar una solución cuya complejidad técnica u operativa sea excesiva para el tamaño actual del proyecto.
* Mantener un equilibrio entre seguridad, costo, confiabilidad, disponibilidad y facilidad de mantenimiento.

## Opciones consideradas

* Wompi.
* ePayco.
* Mercado Pago.

## Resultado de la decisión

Opción elegida: **"Wompi"**, porque ofrece un equilibrio adecuado entre costo, medios de pago disponibles para Colombia, facilidad de integración y herramientas de prueba. Su Plan Avanzado publica una tarifa de **2,65% + $700 COP + IVA por transacción exitosa**, permite recibir pagos mediante tarjetas, PSE, Nequi, DaviPlata y otros medios, y dispone de integración mediante Web Checkout, Widget y API.

Aunque ePayco puede presentar una tarifa ligeramente menor en determinados escenarios, esta depende de condiciones como el banco asociado a la cuenta del comercio. Para cuentas Davivienda publica una tarifa desde **2,64% + $690 COP + IVA**, mientras que para otros bancos publica una tarifa desde **3,29% + $700 COP + IVA**. Mercado Pago publica tarifas superiores cuando se requiere disponibilidad inmediata del dinero, por ejemplo **3,29% + $800 COP + IVA**, aunque ofrece tarifas menores cuando el comercio acepta esperar varios días para disponer de los fondos.

Para MyComplex se prioriza una tarifa predecible, una integración clara con medios de pago locales y la posibilidad de probar transacciones antes de producción. Wompi dispone de ambiente Sandbox independiente del ambiente productivo y permite configurar una URL de eventos para recibir mediante HTTP POST cambios de estado de las transacciones. Esto facilita que MyComplex mantenga actualizado el estado de cada obligación sin depender únicamente de lo que muestre el navegador del usuario.

La integración se realizará desde el backend de MyComplex. El frontend iniciará el proceso de pago, pero el backend será responsable de generar la referencia de la operación, asociarla con el usuario, conjunto residencial y concepto correspondiente, y procesar las confirmaciones enviadas por Wompi. MyComplex no considerará un pago como aprobado únicamente porque el usuario regrese a una pantalla de confirmación; el estado definitivo deberá verificarse mediante la información proporcionada por la pasarela.

### Consecuencias positivas

* Los usuarios podrán realizar pagos en línea sin salir del flujo principal de MyComplex.
* MyComplex no tendrá que almacenar ni procesar directamente información sensible de tarjetas.
* Se ofrecen medios de pago ampliamente utilizados en Colombia.
* El costo por transacción es conocido y puede incluirse dentro del análisis financiero de la plataforma.
* El ambiente Sandbox permite probar pagos aprobados, rechazados y otros escenarios sin utilizar dinero real.
* Los webhooks permiten mantener actualizado el estado de las transacciones de forma automática.
* La integración mediante API sobre HTTPS es compatible con el backend Java y Spring Boot.
* Se mejora la trazabilidad al poder relacionar cada transacción con una referencia interna de MyComplex.

### Consecuencias negativas

* Cada transacción exitosa genera un costo que debe ser asumido por MyComplex, el conjunto residencial o el pagador según el modelo de negocio que se defina.
* MyComplex dependerá de la disponibilidad de un proveedor externo para procesar pagos.
* Se deberán proteger cuidadosamente las llaves privadas y secretos utilizados para integrar la pasarela.
* El backend deberá manejar correctamente pagos pendientes, aprobados, rechazados, duplicados o notificaciones repetidas.
* Será necesario implementar validaciones para evitar registrar dos veces una misma transacción.
* Las tarifas y condiciones comerciales de la pasarela pueden cambiar con el tiempo y deberán revisarse periódicamente.
* La incorporación de pagos aumenta las responsabilidades de auditoría, conciliación y soporte del sistema.

## Pros y contras de las opciones

### Wompi

Pasarela de pagos colombiana que permite recibir pagos mediante diferentes medios y ofrece integración mediante Web Checkout, Widget, API y links de pago.

* Bien, porque publica una tarifa de 2,65% + $700 COP + IVA por transacción exitosa en su Plan Avanzado.
* Bien, porque ofrece medios de pago relevantes para Colombia como PSE, tarjetas, Nequi, DaviPlata y Botón Bancolombia.
* Bien, porque cuenta con un ambiente Sandbox independiente para realizar pruebas sin afectar producción.
* Bien, porque permite configurar webhooks para informar automáticamente cambios de estado de una transacción.
* Bien, porque dispone de documentación técnica para integraciones mediante API.
* Bien, porque el comercio puede recibir el dinero al siguiente día hábil bajo el plan agregador indicado por Wompi.
* Malo, porque cada transacción exitosa tiene una comisión porcentual y un valor fijo adicional.
* Malo, porque genera dependencia de un proveedor externo.
* Malo, porque determinadas funcionalidades y condiciones pueden variar según el plan contratado.

### ePayco

Pasarela colombiana que permite pagos con tarjetas, PSE, billeteras digitales, efectivo y otros medios, mediante Web Checkout, SDK o API.

* Bien, porque para comercios con cuenta Davivienda publica una tarifa desde 2,64% + $690 COP + IVA por transacción exitosa.
* Bien, porque dispone de múltiples medios de pago y herramientas de prevención de fraude.
* Bien, porque ofrece integración mediante API, SDK y Web Checkout.
* Bien, porque no exige tarifa de afiliación ni mantenimiento en el modelo agregador indicado.
* Malo, porque la tarifa depende de las condiciones de vinculación; para cuentas de otros bancos publica una tarifa desde 3,29% + $700 COP + IVA.
* Malo, porque algunas operaciones adicionales, como ciertos retiros, pueden generar costos.
* Malo, porque la estructura de tarifas puede resultar menos predecible para MyComplex si los conjuntos utilizan diferentes entidades bancarias.

### Mercado Pago

Plataforma regional de pagos que permite recibir pagos y ofrece alternativas de disponibilidad del dinero.

* Bien, porque cuenta con una plataforma ampliamente conocida y utilizada en comercio electrónico.
* Bien, porque ofrece pagos en línea y herramientas para cobros recurrentes.
* Bien, porque permite elegir distintos tiempos para disponer del dinero.
* Malo, porque la tarifa publicada para disponibilidad inmediata puede ser superior a Wompi, por ejemplo 3,29% + $800 COP + IVA.
* Malo, porque obtener una tarifa menor puede implicar esperar varios días para disponer del dinero.
* Malo, porque para el alcance colombiano de MyComplex no ofrece una ventaja suficientemente clara frente a Wompi que compense el mayor costo inmediato.

## Enlaces

* Validación principal: [PoC ADR-0027 — Integración Wompi](../PoC/poc-027-wompi/README.md).
