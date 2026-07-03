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
     * project ID, Base64-encoded credentials, and enabled flag
     */
    @Bean
    FirebaseCloudMessagingSettings firebaseMessagingSettings() {
        return new FirebaseCloudMessagingSettings(projectId, credentialsBase64, enabled);
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

        byte[] decodedCredentials = Base64.getDecoder()
                .decode(settings.credentialsBase64().replaceAll("\\s", ""));

        try (ByteArrayInputStream stream = new ByteArrayInputStream(decodedCredentials)) {
            var options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setProjectId(settings.projectId())
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    /**
     * Validates that the Firebase settings required to initialize FCM are present.
     * When FCM integration is enabled, the Firebase project ID and the
     * Base64-encoded service account credentials must be provided.
     *
     * @param settings the FirebaseCloudMessagingSettings to validate
     */
    private void validateSettings(FirebaseCloudMessagingSettings settings) {
        if (settings.projectId() == null || settings.projectId().isBlank()) {
            throw new IllegalStateException(
                    "firebase.project.id is required when integrations.fcm.enabled=true."
            );
        }

        if (settings.credentialsBase64() == null || settings.credentialsBase64().isBlank()) {
            throw new IllegalStateException(
                    "firebase.credentials.base64 is required when integrations.fcm.enabled=true."
            );
        }
    }
}
