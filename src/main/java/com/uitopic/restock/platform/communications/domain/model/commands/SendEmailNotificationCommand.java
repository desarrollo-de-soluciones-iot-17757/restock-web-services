package com.uitopic.restock.platform.communications.domain.model.commands;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Command representing the action of sending an email notification to a user.
 *
 * @param recipientUserId the ID of the user who will receive the email notification. This field is essential for identifying the recipient of the email and ensuring that the notification is sent to the correct user.
 * @param subject the subject line of the email notification. This field is important for providing a clear and concise summary of the email's content, helping the recipient understand the purpose of the email at a glance.
 * @param htmlVariables a list of key-value pairs representing variables that can be used to populate the HTML template of the email. This allows for dynamic content generation in the email body, enabling personalized and context-specific notifications based on the provided variables.
 */
public record SendEmailNotificationCommand(
        String recipientUserId,
        String subject,
        List<Pair<String ,String>> htmlVariables
) {

    public SendEmailNotificationCommand {
        if (recipientUserId == null || recipientUserId.isBlank()) {
            throw new IllegalArgumentException("Recipient user ID cannot be null or blank");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or blank");
        }
        if (htmlVariables == null) {
            throw new IllegalArgumentException("HTML variables list cannot be null");
        }
    }
}
