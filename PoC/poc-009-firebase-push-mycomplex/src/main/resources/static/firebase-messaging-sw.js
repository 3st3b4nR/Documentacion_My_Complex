importScripts(
  "https://www.gstatic.com/firebasejs/10.12.5/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/10.12.5/firebase-messaging-compat.js"
);


firebase.initializeApp({
  apiKey: "AIzaSyCEEbSrmz_N0ely6KY7zX7VG_mkpstK-ig",
  authDomain: "mycomplex-dev.firebaseapp.com",
  projectId: "mycomplex-dev",
  storageBucket: "mycomplex-dev.firebasestorage.app",
  messagingSenderId: "352322053598",
  appId: "1:352322053598:web:e271887d511b0c4534694f",
  measurementId: "G-5GS04PLJ9P"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log(
    "[firebase-messaging-sw.js] Mensaje en segundo plano:",
    payload
  );

  const title = payload.notification?.title || "MyComplex";
  const options = {
    body: payload.notification?.body || "Nueva notificacion"
  };

  self.registration.showNotification(title, options);
});
