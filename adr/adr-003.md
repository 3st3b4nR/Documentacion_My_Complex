# definir la arquitectura para el backend de mycomplex

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-08

## Contexto y planteamiento del problema

Se requiere definir cómo se organizará y estructurará el código del backend de MyComplex para soportar las funcionalidades actuales y el crecimiento futuro. El objetivo es encontrar un equilibrio entre la facilidad de desarrollo, la capacidad de incorporar nuevas funcionalidades sin afectar las existentes y la complejidad de la infraestructura requerida.

## Impulsores de decisión <!-- opcional -->

* Necesidad de incorporar nuevas funcionalidades sin afectar las existentes y mantener la separación de responsabilidades (Atributos: Mantenibilidad, Modificabilidad, Escalabilidad).
* Mantener una complejidad operacional manejable y acorde al tamaño actual del equipo y del proyecto.
* Facilitar el trabajo simultáneo (escalabilidad del equipo) a medida que MyComplex crezca.
* Evitar el alto acoplamiento interno característico de los sistemas maduros no modularizados.

## Opciones consideradas

* Monolito Modular
* Monolito Tradicional por Capas
* Microservicios

## Resultado de la decisión

Opción elegida: "Monolito Modular", porque permite organizar el sistema en módulos independientes según las responsabilidades y dominios del negocio, manteniendo un único despliegue. Esta alternativa reduce la complejidad operacional de los microservicios y evita el acoplamiento del monolito tradicional. Esta decisión se complementará internamente con Arquitectura Hexagonal para cada módulo.

### Consecuencias positivas <!-- opcional -->

* Alta mantenibilidad inicial y alta escalabilidad del equipo a futuro.
* Complejidad operativa reducida al requerir un único despliegue, disminuyendo los costos de infraestructura iniciales frente a sistemas distribuidos.
* La separación lógica facilita una futura migración a microservicios si el sistema lo requiere más adelante.

### Consecuencias negativas <!-- opcional -->

* Exige una disciplina de desarrollo estricta para no romper las fronteras de los módulos y evitar que se convierta en una bola de barro acoplada (Big Ball of Mud).
* El aislamiento de fallos es medio; un error crítico (como un desbordamiento de memoria) en un módulo puede comprometer todo el backend al compartir el mismo proceso.

## Pros y contras de las opciones <!-- opcional -->

### Monolito Modular

Aplicación de un solo despliegue donde el código está dividido en módulos de dominio estrictamente independientes.

* Bien, porque ofrece una alta mantenibilidad inicial y facilita la escalabilidad del equipo.
* Bien, porque reduce la complejidad operacional al tener un despliegue unificado.
* Malo, porque presenta el riesgo de que los desarrolladores rompan las fronteras lógicas, aumentando el acoplamiento interno con el tiempo.

### Monolito Tradicional por Capas

Aplicación agrupada por capas técnicas (Presentación, Lógica, Datos) en lugar de dominios de negocio.

* Bien, porque su complejidad inicial es muy baja y es un patrón ampliamente conocido.
* Malo, porque ofrece una baja escalabilidad del equipo a largo plazo.
* Malo, porque tiende a generar un aumento progresivo del acoplamiento, dificultando el mantenimiento a medida que el sistema crece.

### Microservicios

Arquitectura distribuida donde cada dominio se despliega y escala como un servicio totalmente independiente.

* Bien, porque ofrece un alto aislamiento de fallos y una alta escalabilidad del equipo de desarrollo.
* Malo, porque presenta una complejidad inicial muy alta.
* Malo, porque requiere una infraestructura y una complejidad operacional desproporcionadas para las necesidades actuales del proyecto.

## Enlaces <!-- opcional -->

* [Documento] Drivers Arquitectónicos