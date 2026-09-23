# distribuir las solicitudes entre varias instancias del backend

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Si MyComplex llega a ejecutar varias instancias del backend para atender más usuarios o mantener el servicio disponible ante fallos, las solicitudes deben repartirse entre esas instancias. Se debe definir si hace falta agregar un balanceador independiente o si la plataforma elegida para ejecutar los contenedores ya cubre esta necesidad.

## Impulsores de decisión

* Repartir las solicitudes cuando existan varias instancias del backend.
* Evitar enviar tráfico a una instancia que no esté funcionando correctamente.
* Mantener la arquitectura lo más sencilla posible.
* Aprovechar las capacidades incluidas en la plataforma de contenedores seleccionada.

## Opciones consideradas

* Balanceo integrado de Azure Container Apps
* Azure Application Gateway como balanceador adicional
* Nginx administrado por el equipo

## Resultado de la decisión

Opción elegida: **"Balanceo integrado de Azure Container Apps"**, porque la plataforma ya puede repartir las solicitudes entre las instancias disponibles del backend. Para la arquitectura actual de MyComplex no se justifica agregar otro servicio únicamente para realizar la misma función, lo que permite reducir costos y complejidad.

### Consecuencias positivas

* Se evita agregar un componente adicional para una función que ya ofrece Container Apps.
* Las solicitudes pueden distribuirse entre varias instancias del backend.
* Se simplifica la arquitectura y su mantenimiento.
* Se reducen costos frente a una solución adicional de balanceo.

### Consecuencias negativas

* El comportamiento del balanceo queda ligado a Azure Container Apps.
* Si en el futuro se necesitan reglas de tráfico mucho más avanzadas, podría ser necesario agregar otro componente.
* El equipo tendrá menos control de bajo nivel sobre el balanceo.

## Pros y contras de las opciones

### Balanceo integrado de Azure Container Apps

Función incluida en la plataforma seleccionada para ejecutar los contenedores.

* Bien, porque no requiere administrar otro servicio.
* Bien, porque es suficiente para distribuir el tráfico entre las instancias actuales del backend.
* Bien, porque reduce costos y complejidad.
* Malo, porque ofrece menos control que una solución dedicada.

### Azure Application Gateway como balanceador adicional

Servicio independiente de Azure para controlar y distribuir tráfico web.

* Bien, porque ofrece reglas de tráfico y funciones avanzadas.
* Bien, porque puede ser útil en arquitecturas con necesidades de enrutamiento más complejas.
* Malo, porque actualmente duplicaría funciones que ya ofrece Container Apps.
* Malo, porque aumentaría el costo y la cantidad de componentes a mantener.

### Nginx administrado por el equipo

Servidor que puede configurarse manualmente para distribuir las solicitudes.

* Bien, porque ofrece un alto nivel de control.
* Bien, porque es ampliamente utilizado.
* Malo, porque el equipo tendría que encargarse de su configuración, seguridad y disponibilidad.
* Malo, porque añade trabajo operativo que no es necesario en la etapa actual.

## Enlaces

* Relacionado con el ADR 17 de administración de contenedores.
* Relacionado con los escenarios de disponibilidad, concurrencia y crecimiento de MyComplex.
