package com.mycomplex.pocpush.config;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Bean
    FirebaseApp firebaseApp() throws IOException {

        if (FirebaseApp.getApps().size() > 0) {
            return FirebaseApp.getInstance();
        }

        if (credentialsPath == null || credentialsPath.isBlank()) {
            throw new IllegalStateException(
                "Debe configurar FIREBASE_CREDENTIALS con la ruta del archivo JSON de la cuenta de servicio."
            );
        }

        try (FileInputStream serviceAccount = new FileInputStream(credentialsPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            return FirebaseApp.initializeApp(options);
        }
    }
}
