package com.uitopic.restock.platform.communications.infrastructure.pushnotifications.fcm.configuration;

/**
 * This record represents the settings required to configure Firebase Cloud Messaging (FCM) for push notifications.
 * It includes the project ID,  Base64-encoded credentials, and a flag indicating whether FCM is enabled.
 */
public record FirebaseCloudMessagingSettings(
        String projectId,
        String credentialsBase64,
        String credentialsPath,
        boolean enabled
) {
}
