package com.uitopic.restock.platform.communications.infrastructure.pushnotifications.fcm.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.uitopic.restock.platform.communications.domain.repositories.PushSubscriptionRepository;
import com.uitopic.restock.platform.communications.infrastructure.pushnotifications.fcm.FirebaseCloudMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.fcm.enabled", havingValue = "true")
public class FirebaseCloudMessagingServiceImpl implements FirebaseCloudMessagingService {

    private final FirebaseApp firebaseApp;
    private final PushSubscriptionRepository pushSubscriptionRepository;

    public FirebaseCloudMessagingServiceImpl(
            FirebaseApp firebaseApp,
            PushSubscriptionRepository pushSubscriptionRepository
    ) {
        this.firebaseApp = firebaseApp;
        this.pushSubscriptionRepository = pushSubscriptionRepository;
    }

    @Override
    public void sendPushNotification(String recipientUserId, String deviceToken, String severity, String sourceId, String title, String message) {
        try {
            var pushNotification = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(message)
                            .build())
                    .putData("sourceId", String.valueOf(sourceId))
                    .putData("recipientUserId", String.valueOf(recipientUserId))
                    .build();

            var response = FirebaseMessaging.getInstance(firebaseApp).send(pushNotification);
            log.info("Push notification sent successfully. Firebase message id={}", response);
        } catch (FirebaseMessagingException exception) {
            log.error("Failed to send push notification. user={}, tokenPrefix={}, errorCode={}, message={}",
                    recipientUserId,
                    maskToken(deviceToken),
                    exception.getMessagingErrorCode(),
                    exception.getMessage(),
                    exception);

            if (exception.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                deactivateUnregisteredDevice(
                        recipientUserId,
                        deviceToken
                );
            }
        } catch (Exception exception) {
            log.error("Failed to send push notification to user {} with tokenPrefix={}",
                    recipientUserId,
                    maskToken(deviceToken),
                    exception);
        }
    }

    private void deactivateUnregisteredDevice(
            String recipientUserId,
            String deviceToken
    ) {
        pushSubscriptionRepository
                .findByProviderToken(deviceToken)
                .ifPresentOrElse(device -> {
                    device.deactivate();
                    pushSubscriptionRepository.save(device);
                    log.warn("Deactivated unregistered FCM device token. user={}, notificationDeviceId={}, tokenPrefix={}",
                            recipientUserId,
                            device.getId(),
                            maskToken(deviceToken));
                }, () -> log.warn("UNREGISTERED FCM token was not found in database. user={}, tokenPrefix={}",
                        recipientUserId,
                        maskToken(deviceToken)));
    }

    private String maskToken(String deviceToken) {
        if (deviceToken == null || deviceToken.length() < 12) {
            return "***";
        }
        return deviceToken.substring(0, 8) + "...";
    }
}
