import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.5/firebase-app.js";
import {
  getMessaging,
  getToken,
  onMessage
} from "https://www.gstatic.com/firebasejs/10.12.5/firebase-messaging.js";

import { firebaseConfig, vapidKey } from "./firebase-config.js";

const status = document.getElementById("status");
const tokenArea = document.getElementById("token");
const button = document.getElementById("enableNotifications");

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

button.addEventListener("click", async () => {
  try {
    status.textContent = "Solicitando permiso...";

    const permission = await Notification.requestPermission();

    if (permission !== "granted") {
      status.textContent = "Permiso de notificaciones no concedido.";
      return;
    }

    const registration = await navigator.serviceWorker.register(
      "/firebase-messaging-sw.js"
    );

    const token = await getToken(messaging, {
      vapidKey,
      serviceWorkerRegistration: registration
    });

    if (!token) {
      status.textContent = "Firebase no genero un token.";
      return;
    }

    tokenArea.value = token;
    status.textContent =
      "Token obtenido correctamente. Copialo para probar el backend.";

  } catch (error) {
    console.error(error);
    status.textContent = "Error: " + error.message;
  }
});

onMessage(messaging, (payload) => {
  console.log("Mensaje recibido en primer plano:", payload);

  const title = payload.notification?.title ?? "MyComplex";
  const body = payload.notification?.body ?? "Nueva notificacion";

  status.textContent =
    `Notificacion recibida en primer plano:\n${title}\n${body}`;

  if (Notification.permission === "granted") {
    new Notification(title, { body });
  }
});
