package com.uitopic.restock.platform.communications.application.internal.commandservices;

import com.uitopic.restock.platform.communications.application.internal.outboundservices.acl.ExternalIAMService;
import com.uitopic.restock.platform.communications.application.internal.outboundservices.pushprovider.PushNotificationService;
import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.MarkNotificationAsReadCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendEmailNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendPushNotificationCommand;
import com.uitopic.restock.platform.communications.domain.repositories.NotificationRepository;
import com.uitopic.restock.platform.communications.domain.repositories.PushSubscriptionRepository;
import com.uitopic.restock.platform.communications.domain.services.NotificationCommandService;
import com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.ResendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the NotificationCommandService domain service.
 * This service handles commands related to notifications, such as creating notifications and sending push notifications.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

    /** Repositories for managing notifications and push subscriptions. */
    private final NotificationRepository notificationRepository;

    /** Repository for managing push subscriptions. */
    private final PushSubscriptionRepository pushSubscriptionRepository;

    /** Service for sending push notifications. */
    private final PushNotificationService pushNotificationService;

    /** Service for sending email notifications through Resend. */
    private final ResendService resendService;

    /** Service for interacting with external IAM systems, if needed for notification processing. */
    private final ExternalIAMService externalIAMService;

    /**
     * Handles the CreateNotificationCommand by creating and saving a new notification.
     *
     * @param command the command containing the details for the notification to create
     * @return an Optional containing the created Notification, or empty if creation failed
     */
    @Override
    public Optional<Notification> handle(CreateNotificationCommand command) {

        log.info("Persisting in-app notification. recipientUserId={}, sourceId={}, title={}",
                command.recipientUserId(), command.sourceId(), command.title());
        var notification = new Notification(
                command.recipientUserId(),
                command.sourceId(),
                command.sourceType(),
                command.message(),
                command.title(),
                command.severity()
        );
        return Optional.of(notificationRepository.save(notification));
    }

    /**
     * Handles the MarkNotificationAsReadCommand by marking the specified notification as read.
     *
     * @param command The command containing the details for marking the notification as read.
     * @return an Optional containing the updated Notification, or empty if the notification was not found or could not be updated.
     */
    @Override
    public Optional<Notification> handle(MarkNotificationAsReadCommand command) {

        log.info("Marking notification as read for notification with id {}", command.notificationId());
        if (!notificationRepository.existsById(command.notificationId())) {
            log.warn("Notification with id {} not found. Cannot mark as read.", command.notificationId());
            throw new IllegalArgumentException("Notification with id " + command.notificationId() + " not found.");
        }

        var notification = notificationRepository.findById(command.notificationId());
        notification.markAsRead();
        return Optional.of(notificationRepository.save(notification));
    }

    /**
     * Handles the SendPushNotificationCommand by sending push notifications to all active devices of the recipient user.
     *
     * @param command the command containing the details for the push notification to send
     */
    @Override
    public void handle(SendPushNotificationCommand command) {
        var activeDevices = pushSubscriptionRepository.findByUserIdAndActiveTrueOrderByUpdatedAtDesc(command.recipientUserId());

        log.info("Preparing push notifications. recipientUserId={}, activeDeviceCount={}, sourceId={}, title={}",
                command.recipientUserId(),
                activeDevices.size(),
                command.sourceId(),
                command.title());

        if (activeDevices.isEmpty()) {
            log.info("No active devices found for user. recipientUserId={}", command.recipientUserId());
        }

        log.info("Sending push notification through gateway. recipientUserId={}, sourceId={}, title={}",
                command.recipientUserId(),
                command.sourceId(),
                command.title());

        activeDevices.forEach(device -> pushNotificationService.sendPushNotification(
                command.recipientUserId(),
                device.getProviderToken(),
                command.severity(),
                command.sourceId(),
                command.title(),
                command.message()
        ));
    }

    /**
     * Handles the SendEmailNotificationCommand by sending an email notification to the user associated with the given account ID.
     *
     * @param command The command containing the details for sending the email notification.
     */
    @Override
    public void handle(SendEmailNotificationCommand command) {
        var userEmails = externalIAMService.getUsernamesByAccountId(command.accountId());

        log.info("Preparing email notifications for account with id {}, about {}",
                command.accountId(),
                command.type());

        if (userEmails.isEmpty()) return;

        log.info("Sending email notifications through Resend. accountId={}, receiversNumber={}, about={}",
                command.accountId(),
                userEmails.size(),
                command.type());

        userEmails.forEach(email -> resendService.sendEmail(
                email,
                command.htmlVariables(),
                command.type()
        ));
    }
}
