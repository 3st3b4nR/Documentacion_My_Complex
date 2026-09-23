# Spike ADR-0022/0028 — GitHub Actions, ramificación y versionado

## Hallazgo

ADR-0022 y ADR-0028 seleccionan GitHub Actions para el mismo problema de automatización. No requieren PoC separadas. Se propone conservar ADR-0028 como decisión de CI/CD y hacer que ADR-0022 quede reemplazado o refinado por ella, evitando mantener dos fuentes de verdad.

## Dependencias

* ADR-0016: Docker define el artefacto que construye el pipeline.
* ADR-0020: ACR recibe la imagen versionada.
* ADR-0029: GitHub Flow define cuándo se ejecutan validaciones y despliegues.
* ADR-0030: SemVer define las etiquetas de publicación.

## Artefactos

* `workflow/poc-ci-cd.yml`: workflow de referencia para compilar, probar, construir y publicar.
* `scripts/verify-semver.ps1`: valida etiquetas `vMAJOR.MINOR.PATCH`.
* `docs/evidence-checklist.md`: resultados que deben conservarse.

El workflow se mantiene aquí como evidencia del spike. Para ejecutarlo, deberá copiarse a `.github/workflows/` del repositorio de la aplicación y adaptar la ruta del proyecto Java. El despliegue utiliza OIDC; no se deben guardar credenciales Azure de larga duración en GitHub.

## Criterio de aceptación

Un Pull Request con una prueba fallida no puede integrarse; uno válido produce compilación y pruebas. Una etiqueta SemVer válida publica una imagen con etiqueta y digest rastreables. Una etiqueta inválida falla antes de publicar. El despliegue solo se ejecuta desde el entorno autorizado y utiliza permisos mínimos.

