package com.uitopic.restock.platform.communications.infrastructure.pushnotifications.fcm.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

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
     * The Firebase service account credentials encoded as a Base64 string.
     * This value is read from the application properties and is required when
     * FCM integration is enabled. The decoded content must represent a valid
     * Firebase service account JSON.
     */
    @Value("${firebase.credentials.base64:}")
    private String credentialsBase64;

    /**
     * The path to the Firebase service account credentials JSON file.
     * This value is read from the application properties.
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
     * Creates a bean for FirebaseCloudMessagingSettings, which encapsulates the
     * configuration properties required for FCM integration.
     *
     * @return a new instance of FirebaseCloudMessagingSettings with the configured
     * project ID, Base64-encoded credentials, credentials path, and enabled flag
     */
    @Bean
    FirebaseCloudMessagingSettings firebaseMessagingSettings() {
        return new FirebaseCloudMessagingSettings(projectId, credentialsBase64, credentialsPath, enabled);
    }

    /**
     * Creates a FirebaseApp bean, which is the main entry point for interacting
     * with Firebase services.
     * This bean is only created when FCM integration is enabled. It validates the
     * configured settings, decodes the Firebase service account credentials from
     * Base64, and initializes FirebaseApp with the decoded credentials and project ID.
     *
     * @param settings the FirebaseCloudMessagingSettings containing the required
     *                 FCM configuration values
     * @return an initialized FirebaseApp instance
     * @throws IOException if the decoded credentials cannot be read as valid
     *                     Google credentials
     */
    @Bean
    @ConditionalOnProperty(
            name = "integrations.fcm.enabled",
            havingValue = "true"
    )
    FirebaseApp firebaseApp(FirebaseCloudMessagingSettings settings) throws IOException {
        validateSettings(settings);

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        java.io.InputStream serviceAccount = null;
        if (settings.credentialsBase64() != null && !settings.credentialsBase64().isBlank()) {
            byte[] decodedCredentials = Base64.getDecoder()
                    .decode(settings.credentialsBase64().replaceAll("\\s", ""));
            serviceAccount = new ByteArrayInputStream(decodedCredentials);
        } else if (settings.credentialsPath() != null && !settings.credentialsPath().isBlank()) {
            String path = settings.credentialsPath();
            if (path.startsWith("classpath:")) {
                String resourcePath = path.substring("classpath:".length());
                serviceAccount = FirebaseCloudMessagingConfiguration.class.getClassLoader().getResourceAsStream(resourcePath);
                if (serviceAccount == null) {
                    throw new IOException("Resource not found: " + path);
                }
            } else {
                java.io.File file = new java.io.File(path);
                if (!file.exists()) {
                    // Try classpath as fallback
                    serviceAccount = FirebaseCloudMessagingConfiguration.class.getClassLoader().getResourceAsStream(path);
                    if (serviceAccount == null) {
                        throw new IOException("Credentials file not found at path: " + path);
                    }
                } else {
                    serviceAccount = new java.io.FileInputStream(file);
                }
            }
        }

        if (serviceAccount == null) {
            throw new IllegalStateException("No Firebase credentials provided.");
        }

        try (java.io.InputStream stream = serviceAccount) {
            var options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setProjectId(settings.projectId())
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    /**
     * Validates that the Firebase settings required to initialize FCM are present.
     * When FCM integration is enabled, the Firebase project ID and either the
     * Base64-encoded service account credentials or credentials path must be provided.
     *
     * @param settings the FirebaseCloudMessagingSettings to validate
     */
    private void validateSettings(FirebaseCloudMessagingSettings settings) {
        if (settings.projectId() == null || settings.projectId().isBlank()) {
            throw new IllegalStateException(
                    "firebase.project.id is required when integrations.fcm.enabled=true."
            );
        }

        boolean hasBase64 = settings.credentialsBase64() != null && !settings.credentialsBase64().isBlank();
        boolean hasPath = settings.credentialsPath() != null && !settings.credentialsPath().isBlank();

        if (!hasBase64 && !hasPath) {
            throw new IllegalStateException(
                    "Either firebase.credentials.base64 or firebase.credentials.path is required when integrations.fcm.enabled=true."
            );
        }
    }
}
