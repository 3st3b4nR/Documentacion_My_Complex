# administrar la ejecucion del backend en la nube

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

Después de empaquetar el backend en un contenedor, MyComplex necesita una plataforma que lo ejecute en producción, lo reinicie si presenta un fallo y permita aumentar o reducir la cantidad de instancias según la necesidad. La solución debe evitar que el equipo tenga que administrar una infraestructura demasiado compleja.

## Impulsores de decisión

* Mantener el backend disponible y reiniciarlo automáticamente cuando sea necesario.
* Permitir aumentar la capacidad de la aplicación cuando crezca el número de usuarios.
* Reducir el trabajo de administración de servidores.
* Mantener una solución adecuada para un monolito modular y un equipo pequeño.
* Permitir comenzar con un costo reducido mediante un modelo de consumo y escalado a cero.

## Opciones consideradas

* Azure Container Apps
* Azure Kubernetes Service (AKS)
* Máquinas virtuales administradas directamente por el equipo

## Resultado de la decisión

Opción elegida: **"Azure Container Apps"**, porque permite ejecutar contenedores, crear varias instancias cuando sea necesario y reiniciarlas ante fallos sin que el equipo tenga que administrar un clúster de Kubernetes. Esto ofrece las capacidades que MyComplex necesita actualmente con una complejidad menor y se integra con los demás servicios seleccionados en Azure.

### Consecuencias positivas

* El equipo no necesita administrar servidores ni un clúster de Kubernetes.
* La aplicación puede aumentar o reducir sus instancias según la demanda.
* Se simplifican las actualizaciones y nuevas versiones del backend.
* Se integra con el registro de contenedores, monitoreo e identidad de Azure.
* El plan de consumo ofrece una asignación gratuita mensual y permite escalar a cero cuando no existen solicitudes, favoreciendo la etapa académica y de bajo tráfico de MyComplex.

### Consecuencias negativas

* La solución depende de Azure.
* Será necesario aprender la configuración propia de Container Apps.
* Algunas configuraciones avanzadas ofrecen menos control que administrar Kubernetes directamente.

## Pros y contras de las opciones

### Azure Container Apps

Servicio administrado que ejecuta y escala aplicaciones empaquetadas en contenedores.

* Bien, porque reduce el trabajo de administración de infraestructura.
* Bien, porque puede aumentar o reducir instancias automáticamente.
* Bien, porque se adapta bien al tamaño actual de MyComplex.
* Malo, porque genera dependencia de Azure.

### Azure Kubernetes Service (AKS)

Servicio administrado para ejecutar Kubernetes en Azure.

* Bien, porque ofrece un alto nivel de control y flexibilidad para sistemas distribuidos grandes.
* Bien, porque es adecuado cuando existen muchos servicios independientes.
* Malo, porque añade una complejidad considerable para un único backend modular.
* Malo, porque requiere más conocimientos y trabajo operativo del equipo.

### Máquinas virtuales administradas directamente por el equipo

Consiste en ejecutar los contenedores sobre servidores que el equipo debe configurar y mantener.

* Bien, porque ofrece control directo sobre los servidores.
* Bien, porque permite personalizar completamente el entorno.
* Malo, porque el equipo debe encargarse de actualizaciones, disponibilidad y escalado.
* Malo, porque aumenta el trabajo operativo sin aportar una ventaja clara al proyecto actual.

## Enlaces

* Relacionado con el ADR 16 de Docker.
* Relacionado con el ADR 18 de distribución de solicitudes.
* Relacionado con el ADR 20 de Container Registry.
