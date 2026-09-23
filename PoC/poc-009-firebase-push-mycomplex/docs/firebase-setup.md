# Configuración de Firebase — PoC ADR-009

## 1. Crear proyecto

En Firebase Console crea un proyecto, por ejemplo:

```text
mycomplex-dev
```

## 2. Registrar aplicación Web

En:

```text
Project settings
→ General
→ Your apps
→ Web
```

Registra una aplicación.

Firebase mostrará un objeto parecido a:

```javascript
const firebaseConfig = {
  apiKey: "...",
  authDomain: "...",
  projectId: "...",
  storageBucket: "...",
  messagingSenderId: "...",
  appId: "..."
};
```

Copia estos valores en:

```text
src/main/resources/static/js/firebase-config.js
```

y también en:

```text
src/main/resources/static/firebase-messaging-sw.js
```

Estos valores corresponden a la configuración pública de la aplicación Web.

## 3. Crear clave Web Push / VAPID

En:

```text
Project settings
→ Cloud Messaging
→ Web Push certificates
```

Genera un par de claves si no existe.

Copia la clave pública en:

```text
src/main/resources/static/js/firebase-config.js
```

como:

```javascript
export const vapidKey = "...";
```

## 4. Crear cuenta de servicio para backend

En:

```text
Project settings
→ Service accounts
→ Firebase Admin SDK
→ Generate new private key
```

Descarga el JSON.

Guárdalo FUERA del repositorio.

Ejemplo:

```text
C:\\Users\\tu_usuario\\Documents\\firebase-service-account.json
```

Nunca lo publiques en GitHub.

## 5. Ejecutar

PowerShell:

```powershell
$env:FIREBASE_CREDENTIALS="C:\Users\lucua\OneDrive\Desktop\mycomplex-dev-firebase-adminsdk-fbsvc-655636d0db.json"
mvn spring-boot:run
```

## 6. Abrir navegador

```text
http://localhost:8082
```

Pulsa el botón para solicitar permisos y obtener el token FCM.

## Nota sobre localhost

Los Service Workers y las Web Push Notifications requieren un contexto seguro. Los navegadores modernos tratan `localhost` como contexto seguro para desarrollo.

En producción deberá utilizarse HTTPS.
