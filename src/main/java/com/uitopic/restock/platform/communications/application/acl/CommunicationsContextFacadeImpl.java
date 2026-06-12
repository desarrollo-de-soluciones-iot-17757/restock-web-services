package com.uitopic.restock.platform.communications.application.acl;

import com.uitopic.restock.platform.communications.domain.model.commands.CreateNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendEmailNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.commands.SendPushNotificationCommand;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.services.NotificationCommandService;
import com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.services.ResendEmailBuilder;
import com.uitopic.restock.platform.communications.interfaces.acl.CommunicationsContextFacade;
import com.uitopic.restock.platform.shared.domain.model.commands.NotificationCommand;
import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CommunicationsContextFacade, providing methods to interact with communication-related services.
 * This class serves as a facade for the communications bounded context, allowing other bounded contexts to interact with it without needing to know about its internal workings.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommunicationsContextFacadeImpl implements CommunicationsContextFacade {

    /** Service for handling notification commands. */
    private final NotificationCommandService notificationCommandService;

    /**
     * Processes a notification command by sending a push notification when the notification type allows it.
     *
     * @param command the notification command containing the account ID, notification type, and event payload
     */
    @Override
    public void processNotification(NotificationCommand command) {
        log.info("Processing notification about an alert in the system for recipient={}, about {}",
                command.accountId().getAccountId(),
                command.event().getClass().getSimpleName());

        // TODO: Type of notification should be determined based on Profiles Context not from the command.
        var notificationPreference = NotificationType.ALL;

        // If the notification type is NONE, any notification will be sent.
        if (notificationPreference == NotificationType.NONE) {
            log.info("Notification type is NONE, skipping notification.");
            return;
        }

        // If the notification preference is ALL, both in-app and push notifications will be sent.
        // If it's PUSH, only push notifications will be sent.
        // If it's EMAIL, only email notifications will be sent.
        switch (notificationPreference) {
            case NotificationType.ALL:
                log.info("Notification type is ALL, sending both email and push notifications.");
                sendPushNotification(command);
                sendEmailNotification(command);
                break;
            case NotificationType.PUSH:
                log.info("Notification type is PUSH, sending push notification.");
                sendPushNotification(command);
                break;
            case NotificationType.EMAIL:
                log.info("Notification type is EMAIL, sending email notification.");
                sendEmailNotification(command);
                break;
        }

        // In-app notifications will be created regardless of the notification preference, as they are a core part of the user experience for alerts in the system.
        createInAppNotification(command);
    }

    /**
     * Creates an in-app notification based on the provided NotificationCommand. This method constructs a CreateNotificationCommand using the account ID, event class name as the source ID, and default values for the title, message, and severity. It then delegates the handling of the in-app notification to the NotificationCommandService.
     *
     * @param command the NotificationCommand containing the account ID and the notification event for which the in-app notification is to be created. The method extracts necessary information from the command to create a CreateNotificationCommand, which is then processed by the NotificationCommandService to create and persist the in-app notification for the appropriate recipient.
     */
    private void createInAppNotification(NotificationCommand command) {
        CreateNotificationCommand createNotificationCommand = new CreateNotificationCommand(
                command.accountId().getAccountId(),
                command.event().getClass().getSimpleName(),
                "",
                "",
                NotificationSeverity.INFO.name()
        );

        notificationCommandService.handle(createNotificationCommand);
    }

    /**
     * Sends a push notification based on the provided NotificationCommand. This method constructs a SendPushNotificationCommand using the account ID, event class name as the source ID, and default values for the title, message, and severity. It then delegates the handling of the push notification to the NotificationCommandService.
     *
     * @param command the NotificationCommand containing the account ID and the notification event for which the push notification is to be sent. The method extracts necessary information from the command to create a SendPushNotificationCommand, which is then processed by the NotificationCommandService to send the push notification to the appropriate recipients.
     */
    private void sendPushNotification(NotificationCommand command) {
        var recipientAccount = command.accountId();
        var sourceId = command.event().getClass().getSimpleName();
        var title = "";
        var message = "";
        var severity = NotificationSeverity.INFO.name();

        SendPushNotificationCommand sendPushCommand = new SendPushNotificationCommand(
                recipientAccount.getAccountId(),
                sourceId,
                title,
                message,
                severity
        );

        notificationCommandService.handle(sendPushCommand);
    }

    /**
     * Sends an email notification based on the provided NotificationCommand. This method constructs a SendEmailNotificationCommand using the account ID, converts the attributes of the notification event into email template variables, and analyzes the alert type from the event. It then delegates the handling of the email notification to the NotificationCommandService.
     *
     * @param command the NotificationCommand containing the account ID and the notification event for which the email notification is to be sent. The method extracts necessary information from the command to create a SendEmailNotificationCommand, which is then processed by the NotificationCommandService to send the email notification to the appropriate recipients.
     */
    private void sendEmailNotification(NotificationCommand command) {
        SendEmailNotificationCommand sendEmailCommand = new SendEmailNotificationCommand(
                command.accountId(),
                convertObjectAttributesToEmailTemplateVariables(command.event()),
                analyzeAlertType(command.event())
        );

        notificationCommandService.handle(sendEmailCommand);
    }

    /**
     * Converts the attributes of a NotificationEvent object into a list of key-value pairs that can be used as variables in an email template. This method uses reflection to access the fields of the event object and creates pairs where the key is the field name converted to UPPER_SNAKE_CASE format and the value is the string representation of the field's value. This allows for dynamic generation of email content based on the properties of the notification event.
     *
     * @param event the NotificationEvent object whose attributes are to be converted into email template variables. This object contains the data related to the notification event, and its fields will be accessed and transformed into key-value pairs for use in email templates.
     * @return a list of pairs where each pair consists of a variable name in UPPER_SNAKE_CASE format and its corresponding value as a string. This list can be used to populate email templates with dynamic content based on the attributes of the notification event.
     */
    private List<Pair<String, String>> convertObjectAttributesToEmailTemplateVariables(NotificationEvent event) {
        List<Pair<String, String>> pairs = new ArrayList<>();

        if (event == null) {
            return pairs;
        }

        Field[] fields = event.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                String name = toUpperSnakeCase(field.getName());
                Object value = field.get(event);

                pairs.add(
                        Pair.of(
                                name,
                                value != null ? value.toString() : null
                        )
                );
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "Error reading field: " + field.getName(), e
                );
            }
        }

        return pairs;
    }

    /**
     * Utility method to convert a variable name from PascalCase format to UPPER_SNAKE_CASE format, which is commonly used for email template variables. This method splits the input variable name into parts based on uppercase letters, underscores, spaces, or hyphens, and then converts each part to uppercase before joining them together.
     *
     * @param variable the variable name in PascalCase format (e.g., "stockLevel", "batchNumber")
     * @return the variable name converted to UPPER_SNAKE_CASE format (e.g., "STOCK_LEVEL", "BATCH_NUMBER"). If the input variable is null or blank, it will be returned as is.
     */
    private String toUpperSnakeCase(String variable) {
        if (variable == null || variable.isBlank()) {
            return variable;
        }

        return variable
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2") // stockLevel -> stock_Level
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2") // StockLevel -> Stock_Level
                .toUpperCase(); // stock_Level -> STOCK_LEVEL
    }

    /**
     * Analyzes the type of alert based on the class name of the NotificationEvent. The method extracts the alert type by taking the substring of the event's class name up to the first uppercase letter after the first character. For example, if the event class name is "InventoryStockLevelAlertEvent", the extracted alert type would be "Inventory". If no specific type can be determined from the class name, the original class name will be returned.
     * This allows for categorization and processing of alerts based on their types, which can be useful for determining the appropriate handling and notification strategies for different kinds of alerts in the system.
     *
     * @param event the notification event for which the alert type is to be analyzed. This event contains information about the specific alert that occurred in the system, and its class name will be used to determine the type of alert for categorization and processing purposes.
     * @return the extracted alert type based on the event's class name (e.g., "Inventory" for "InventoryStockLevelAlertEvent") or the original class name if no specific type can be determined
     */
    private String analyzeAlertType(NotificationEvent event) {
        String eventFullName = event.getClass().getSimpleName();
        String eventType = getEventTypeFromEventFullName(eventFullName);

        if (eventType.equals(eventFullName)) return "";

        return eventType;
    }

    /**
     * Extracts the event type from the full name of the event class. The event type is determined by taking the substring of the event's class name up to the first uppercase letter after the first character.
     *
     * @param eventFullName the full name of the event class (e.g., "InventoryStockLevelAlertEvent")
     * @return the extracted event type (e.g., "Inventory") or the original full name if no uppercase letter is found after the first character
     */
    private String getEventTypeFromEventFullName(String eventFullName) {
        if (eventFullName == null || eventFullName.isEmpty()) {
            return eventFullName;
        }

        int endIndex = -1;
        for (int i = 1; i < eventFullName.length(); i++) {
            if (Character.isUpperCase(eventFullName.charAt(i))) {
                endIndex = i;
                break;
            }
        }

        if (endIndex == -1) {
            return eventFullName;
        }

        return eventFullName.substring(0, endIndex);
    }
}
