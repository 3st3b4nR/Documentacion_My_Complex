# PoC ADR-016 — Empaquetar el backend con Docker

## Objetivo

Esta prueba de concepto valida la decisión del ADR-016 de utilizar **Docker** para empaquetar el backend de MyComplex.

El ADR busca que el backend pueda ejecutarse de manera consistente entre desarrollo, pruebas y producción, reduciendo diferencias entre equipos y entornos. La opción seleccionada fue Docker. fileciteturn27file1

La PoC valida este flujo:

```text
Código Spring Boot
      ↓
Dockerfile
      ↓
docker build
      ↓
Imagen Docker
      ↓
docker run
      ↓
Backend funcionando dentro de un contenedor
```

---

## Qué demuestra

Esta PoC permite comprobar que:

1. El backend de Spring Boot puede empaquetarse dentro de una imagen Docker.
2. La imagen contiene lo necesario para ejecutar la aplicación.
3. El mismo artefacto puede ejecutarse de forma aislada en un contenedor.
4. La configuración puede modificarse mediante variables de entorno.
5. El puerto interno puede exponerse hacia el equipo anfitrión.
6. La aplicación sigue respondiendo correctamente después de ser contenedorizada.
7. La imagen puede identificarse mediante tags/versiones.

---

## Qué decisión estamos probando

La decisión del ADR-016 es:

```text
Docker
```

frente a alternativas como:

```text
Podman
Despliegue directo del JAR
```

El ADR seleccionó Docker porque permite repetir el mismo entorno de ejecución y facilita los despliegues posteriores. fileciteturn27file1

Esta PoC no compara de nuevo las tres alternativas; demuestra que la opción elegida es técnicamente viable.

---

## Estructura

```text
poc-016-docker-mycomplex/
├── docs/
│   ├── docker-setup.md
│   └── evidence-checklist.md
├── scripts/
│   ├── build-image.ps1
│   ├── run-container.ps1
│   └── test-container.ps1
├── src/
│   └── main/
│       ├── java/com/mycomplex/pocdocker/
│       │   ├── PocDockerApplication.java
│       │   └── controller/
│       │       └── DockerController.java
│       └── resources/
│           └── application.properties
├── .dockerignore
├── .gitignore
├── compose.yaml
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Dockerfile

Se utiliza un Dockerfile **multi-stage**.

### Etapa 1

```text
Maven + Java 21
```

se utiliza solamente para:

```text
compilar
+
generar el JAR
```

### Etapa 2

La imagen final utiliza únicamente:

```text
Java 21 JRE
+
app.jar
```

Conceptualmente:

```text
Imagen de construcción
        ↓
     app.jar
        ↓
Imagen de ejecución
```

Esto evita dejar Maven y archivos de construcción innecesarios dentro de la imagen final.

---

## Usuario no root

El contenedor se ejecuta con:

```text
USER mycomplex
```

y no con el usuario `root`.

Esto es una buena práctica de seguridad para la imagen.

---

## Construir la imagen

Primero necesitas tener Docker funcionando.

Desde la raíz:

```powershell
docker build -t mycomplex/poc-016-docker:1.0.0 .
```

O utiliza:

```powershell
.\scripts\build-image.ps1
```

Esperado:

```text
Successfully built ...
Successfully tagged mycomplex/poc-016-docker:1.0.0
```

Según la versión de Docker, el texto mostrado puede variar.

---

## Verificar imagen

```powershell
docker images
```

Debe aparecer:

```text
mycomplex/poc-016-docker
```

con el tag:

```text
1.0.0
```

---

## Ejecutar contenedor

```powershell
docker run --name mycomplex-poc-016 `
  -p 8087:8080 `
  -e APP_ENVIRONMENT=docker `
  mycomplex/poc-016-docker:1.0.0
```

El mapeo significa:

```text
PC:8087
   ↓
contenedor:8080
```

---

## Probar

En otra terminal:

```powershell
Invoke-RestMethod http://localhost:8087/health
```

Esperado:

```json
{
  "status": "UP",
  "service": "poc-016-docker"
}
```

Luego:

```powershell
Invoke-RestMethod http://localhost:8087/api/info
```

Ejemplo:

```json
{
  "application": "MyComplex",
  "poc": "ADR-016 Docker",
  "environment": "docker",
  "hostname": "a12bc34...",
  "javaVersion": "21...",
  "timestamp": "..."
}
```

El `hostname` normalmente corresponde al identificador interno del contenedor, lo que sirve como evidencia de que la aplicación está ejecutándose dentro de Docker.

---

## Variables de entorno

En local la aplicación utiliza:

```text
APP_ENVIRONMENT=local
```

Dentro de Docker podemos cambiarlo:

```powershell
-e APP_ENVIRONMENT=pruebas
```

y luego:

```text
GET /api/info
```

debe mostrar:

```json
{
  "environment": "pruebas"
}
```

Esto demuestra que la imagen puede mantenerse igual mientras la configuración cambia entre ambientes.

---

## Docker Compose

También se incluye:

```text
compose.yaml
```

Puedes ejecutar:

```powershell
docker compose up --build
```

y después:

```text
http://localhost:8087/health
```

Para detener:

```powershell
docker compose down
```

Docker Compose se incluye únicamente como forma sencilla de ejecutar localmente la PoC. No es una decisión arquitectónica adicional.

---

## Prueba de reproducibilidad

Una prueba útil consiste en:

```text
1. construir la imagen;
2. ejecutar el contenedor;
3. detenerlo;
4. eliminar el contenedor;
5. volver a ejecutar exactamente la misma imagen;
6. comprobar que responde igual.
```

Comandos:

```powershell
docker stop mycomplex-poc-016
docker rm mycomplex-poc-016

docker run --name mycomplex-poc-016 `
  -p 8087:8080 `
  mycomplex/poc-016-docker:1.0.0
```

Esto ayuda a demostrar la reproducibilidad buscada por el ADR. fileciteturn27file1

---

## Qué NO valida esta PoC

Esta prueba no valida:

```text
- Azure Container Apps;
- Azure Container Registry;
- GitHub Actions;
- balanceo de carga;
- escalado automático;
- Key Vault;
- Auth0;
- PostgreSQL.
```

Esas decisiones corresponden a otros ADR.

En particular:

```text
ADR-016 → cómo empaquetamos el backend.
ADR-017 → dónde ejecutamos ese contenedor.
ADR-020 → dónde almacenamos/versionamos la imagen.
```

Mantenerlos separados evita mezclar decisiones arquitectónicas.

---

## Criterios de éxito

La PoC se considera exitosa si:

```text
✓ docker build crea una imagen correctamente;
✓ la imagen puede ejecutarse mediante docker run;
✓ el backend responde desde dentro del contenedor;
✓ /health devuelve 200;
✓ /api/info identifica el entorno del contenedor;
✓ las variables de entorno modifican configuración sin reconstruir la imagen;
✓ el mismo tag puede volver a ejecutarse de forma reproducible;
✓ la aplicación no necesita instalar Java manualmente en el equipo de ejecución fuera del contenedor.
```

Si se cumplen estos puntos, se confirma la viabilidad técnica de Docker para empaquetar el backend de MyComplex.
