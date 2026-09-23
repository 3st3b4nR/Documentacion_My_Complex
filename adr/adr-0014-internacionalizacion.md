# mostrar la plataforma en espanol e ingles

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex deberá poder utilizarse en español e inglés. Se necesita una forma ordenada de guardar los textos de la aplicación para que cambiar de idioma no obligue a duplicar pantallas ni escribir condiciones de idioma por todo el código.

## Impulsores de decisión

* Permitir que el usuario seleccione español o inglés.
* Mantener los textos de cada idioma organizados y fáciles de modificar.
* Evitar duplicar código por cada idioma.
* Mantener una solución sencilla y de bajo costo para los dos idiomas definidos actualmente.

## Opciones consideradas

* Archivos de mensajes dentro del frontend y backend
* Servicio externo de gestión de traducciones
* Guardar los mensajes en PostgreSQL

## Resultado de la decisión

Opción elegida: **"Archivos de mensajes dentro del frontend y backend"**, porque permite guardar las traducciones de español e inglés en archivos organizados junto con la aplicación. Esta alternativa es suficiente para los dos idiomas definidos, no requiere contratar otro servicio y facilita que el equipo mantenga las traducciones junto con el código.

### Consecuencias positivas

* Los textos de español e inglés quedan centralizados y organizados.
* Se evita repetir mensajes directamente en diferentes partes del código.
* No se necesita pagar ni mantener un servicio externo de traducción.
* En el futuro se pueden agregar nuevos idiomas siguiendo la misma estructura.

### Consecuencias negativas

* Cambiar una traducción normalmente requerirá publicar una nueva versión de la aplicación.
* El frontend y el backend deberán seguir las mismas reglas para nombrar los mensajes.
* Se deberá guardar la preferencia de idioma de cada usuario.

## Pros y contras de las opciones

### Archivos de mensajes dentro del frontend y backend

Archivos separados que contienen los textos en español e inglés.

* Bien, porque es una solución simple y suficiente para dos idiomas.
* Bien, porque las traducciones se guardan y revisan junto con el código.
* Bien, porque no agrega costos de infraestructura.
* Malo, porque un cambio de traducción normalmente necesita un nuevo despliegue.

### Servicio externo de gestión de traducciones

Plataforma especializada para administrar traducciones fuera del código de MyComplex.

* Bien, porque facilita el trabajo cuando existen muchos idiomas o traductores externos.
* Bien, porque algunas plataformas permiten cambiar traducciones sin modificar el código.
* Malo, porque agrega costos y otra herramienta que el equipo debe aprender y administrar.
* Malo, porque resulta innecesario para español e inglés en la etapa actual.

### Guardar los mensajes en PostgreSQL

Consiste en almacenar las traducciones dentro de tablas de la base de datos.

* Bien, porque permitiría modificar textos sin publicar una nueva versión.
* Bien, porque los mensajes quedarían centralizados.
* Malo, porque añade consultas y lógica adicional para algo que puede resolverse con archivos sencillos.
* Malo, porque mezcla los textos de la interfaz con los datos principales del negocio.

## Enlaces

* Relacionado con el ADR 08 de Java y Spring Boot.
* Relacionado con el ADR 11 pendiente para seleccionar el framework del frontend.
* Requiere una historia de usuario para seleccionar español o inglés.
