package com.uitopic.restock.platform.communications.infrastructure.pushnotifications.fcm.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration class for setting up Firebase Cloud Messaging (FCM) integration.
 * This class reads the necessary configuration properties for FCM, validates them,
 * and initializes the FirebaseApp bean if FCM integration is enabled.
 */
@Configuration
public class FirebaseCloudMessagingConfiguration {

    /**
     * The Firebase project ID, read from the application properties.
     * This is required for initializing the FirebaseApp and must be provided when FCM integration is enabled.
     */
    @Value("${firebase.project.id:}")
    private String projectId;

    /**
     * The path to the Firebase service account credentials JSON file, read from the application properties.
     * This is required for initializing the FirebaseApp and must point to a valid JSON file when FCM integration is enabled.
     */
    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    /**
     * Flag indicating whether FCM integration is enabled, read from the application properties.
     * If set to true, the FirebaseApp bean will be initialized. If false, it will not be created.
     */
    @Value("${integrations.fcm.enabled:false}")
    private boolean enabled;

    /**
     * Creates a bean for FirebaseCloudMessagingSettings, which encapsulates the configuration properties for FCM integration.
     * This bean will be used to provide the necessary settings for initializing the FirebaseApp.
     *
     * @return a new instance of FirebaseCloudMessagingSettings with the configured properties
     */
    @Bean
    FirebaseCloudMessagingSettings firebaseMessagingSettings() {
        return new FirebaseCloudMessagingSettings(projectId, credentialsPath, enabled);
    }

    /**
     * Creates a bean for FirebaseApp, which is the main entry point for interacting with Firebase services.
     * This bean will only be created if FCM integration is enabled. It validates the configuration properties
     * and initializes the FirebaseApp with the provided credentials and project ID.
     *
     * @param settings the FirebaseCloudMessagingSettings containing the necessary configuration properties
     * @return an initialized FirebaseApp instance if FCM integration is enabled; otherwise, no bean is created
     * @throws IOException if there is an error reading the credentials file
     */
    @Bean
    FirebaseApp firebaseApp(FirebaseCloudMessagingSettings settings) throws IOException {
        if (!settings.enabled()) {
            throw new IllegalStateException("FirebaseApp should not be created when FCM integration is disabled.");
        }

        validateCredentialsPath(settings.credentialsPath());

        if (FirebaseApp.getApps().isEmpty()) {
            try (var serviceAccount = new FileInputStream(settings.credentialsPath())) {
                var options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId(settings.projectId())
                        .build();

                return FirebaseApp.initializeApp(options);
            }
        }
        return FirebaseApp.getInstance();
    }

    /**
     * Validates that the configured Firebase credentials path points to one concrete existing file.
     *
     * @param credentialsPath configured credentials path
     */
    private void validateCredentialsPath(String credentialsPath) {
        if (credentialsPath == null || credentialsPath.isBlank()) {
            throw new IllegalStateException("firebase.credentials.path is required when integrations.fcm.enabled=true.");
        }
        if (credentialsPath.contains("*") || credentialsPath.contains("?")) {
            throw new IllegalStateException(
                    "firebase.credentials.path must point to one concrete JSON file. Wildcards are not supported: " + credentialsPath
            );
        }

        var path = Path.of(credentialsPath);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "firebase.credentials.path must point to an existing JSON file. Current value: " + credentialsPath
            );
        }
    }
}
