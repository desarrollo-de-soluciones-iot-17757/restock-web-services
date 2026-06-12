package com.uitopic.restock.platform.communications.domain.services;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendEmailNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendPushNotificationCommand;

import java.util.Optional;

/**
 * Service interface for handling commands related to notifications.
 * This service defines methods for creating notifications and sending push notifications.
 */
public interface NotificationCommandService {

    /**
     * Handles the creation of a new notification based on the provided command.
     *
     * @param command The command containing the details for creating the notification.
     * @return An Optional containing the created Notification if successful, or empty if creation failed.
     */
    Optional<Notification> handle(CreateNotificationCommand command);

    /**
     * Handles the sending of a push notification based on the provided command.
     *
     * @param command The command containing the details for sending the push notification.
     */
    void handle(SendPushNotificationCommand command);

    /**
     * Handles the sending of an email notification based on the provided command.
     *
     * @param command The command containing the details for sending the email notification.
     */
    void handle(SendEmailNotificationCommand command);
}
