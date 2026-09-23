# almacenar las imagenes de las noticias

* Estado: propuesto
* Decidentes: Jaider Sebastián Lucuara Cortés - John Esteban Roldán Marín
* Fecha: 2026-09-22

## Contexto y planteamiento del problema

MyComplex permitirá que los administradores publiquen noticias acompañadas de una imagen. Se debe definir dónde guardar esas imágenes para que no aumenten innecesariamente el tamaño de la base de datos y para que cada conjunto residencial mantenga sus archivos separados de los demás.

## Impulsores de decisión

* Guardar las imágenes del portal de noticias de forma segura.
* Mantener las imágenes separadas según el conjunto residencial al que pertenecen.
* Evitar usar PostgreSQL como almacenamiento principal de archivos.
* Permitir que el espacio destinado a imágenes pueda crecer sin afectar el funcionamiento de la base de datos.

## Opciones consideradas

* Azure Blob Storage mediante Storage Account
* Amazon S3
* Guardar las imágenes dentro de PostgreSQL

## Resultado de la decisión

Opción elegida: **"Azure Blob Storage mediante Storage Account"**, porque está diseñado para almacenar archivos como imágenes, permite mantenerlos separados de los datos normales de PostgreSQL y se integra con los demás servicios de Azure seleccionados para MyComplex. Además, facilita organizar los archivos por conjunto residencial y controlar quién puede acceder a ellos.

### Consecuencias positivas

* Las imágenes no aumentan innecesariamente el tamaño de PostgreSQL.
* Los archivos se pueden organizar por conjunto residencial y por noticia.
* El almacenamiento puede crecer sin afectar directamente la base de datos principal.
* Se pueden aplicar reglas de acceso y eliminación específicas para los archivos.

### Consecuencias negativas

* Se agrega un servicio adicional que tendrá un costo según el uso.
* Se deberán validar el tamaño y tipo de las imágenes antes de guardarlas.
* Se deberá evitar que un usuario pueda acceder a archivos pertenecientes a otro conjunto residencial.

## Pros y contras de las opciones

### Azure Blob Storage mediante Storage Account

Servicio de Azure pensado para almacenar archivos como imágenes, documentos y otros contenidos.

* Bien, porque permite guardar las imágenes fuera de PostgreSQL.
* Bien, porque se integra con los demás servicios de Azure del proyecto.
* Bien, porque puede crecer de acuerdo con la cantidad de imágenes almacenadas.
* Malo, porque genera un costo adicional de almacenamiento y transferencia.
* Malo, porque requiere definir correctamente los permisos de acceso.

### Amazon S3

Servicio de almacenamiento de archivos ofrecido por AWS.

* Bien, porque es una solución madura y ampliamente utilizada.
* Bien, porque permite controlar el acceso y el ciclo de vida de los archivos.
* Malo, porque introduciría un segundo proveedor de nube si el resto de MyComplex se aloja en Azure.
* Malo, porque aumentaría la cantidad de cuentas, configuraciones y costos que el equipo debe administrar.

### Guardar las imágenes dentro de PostgreSQL

Consiste en guardar directamente los archivos de imagen dentro de la base de datos.

* Bien, porque evita agregar otro servicio.
* Bien, porque la noticia y su imagen quedarían almacenadas en el mismo lugar.
* Malo, porque aumentaría considerablemente el tamaño de la base de datos.
* Malo, porque las copias de seguridad serían más pesadas y el almacenamiento sería menos flexible.

## Enlaces


* Validación relacionada: [PoC ADR-0020/0021/0023/0024 — Infraestructura Azure](../PoC/poc-020-024-infraestructura-azure/README.md).
* Relacionado con el ADR 01 de PostgreSQL multi-tenant.
* Relacionado con el ADR 19 de protección de secretos y credenciales.
* Requiere una historia de usuario para adjuntar imágenes al publicar o editar noticias.
