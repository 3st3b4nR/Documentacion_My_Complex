# Evidencia — Infraestructura Azure

* Fecha:
* Responsable:
* Región:
* Versiones de Azure CLI, Docker y PostgreSQL:
* Commit e imagen evaluados:

| Prueba | Resultado esperado | Evidencia | Resultado |
| --- | --- | --- | --- |
| Publicar imagen con etiqueta inmutable | La imagen existe en ACR | Digest y captura/salida sin secretos | Pendiente |
| Desplegar desde ACR | Existe una revisión saludable | Revisión y estado | Pendiente |
| Retirar `AcrPull` temporalmente | El nuevo despliegue falla por autorización | Error saneado | Pendiente |
| Leer secreto autorizado | La aplicación obtiene el valor sin credencial almacenada | Traza sin revelar valor | Pendiente |
| Leer secreto no autorizado | Azure responde acceso denegado | Código/resultado | Pendiente |
| Escribir Blob en contenedor permitido | La operación finaliza correctamente | Identificador del blob | Pendiente |
| Acceder a contenedor no permitido | Azure responde acceso denegado | Código/resultado | Pendiente |
| Conectar a PostgreSQL desde origen permitido | Conexión exitosa con TLS verificado | Resultado de `ssl_is_used()` o equivalente | Pendiente |
| Conectar desde origen no permitido | Firewall rechaza la conexión | Error saneado | Pendiente |
| Restauración a un momento anterior | Nueva instancia contiene el registro de control | Consulta y tiempo de recuperación | Pendiente |
| Revisar repositorio y logs | No aparecen secretos | Resultado del escaneo | Pendiente |

## Conclusión

Pendiente. No marcar los ADR como validados hasta completar las pruebas positivas y negativas.

