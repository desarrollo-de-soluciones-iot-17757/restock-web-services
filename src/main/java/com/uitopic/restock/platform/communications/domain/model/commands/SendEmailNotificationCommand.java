package com.uitopic.restock.platform.communications.domain.model.commands;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Command representing the action of sending an email notification to a user.
 *
 * @param accountId the ID of the account to which the email notification will be sent. This field is crucial for identifying the recipient of the email and ensuring that the notification is delivered to the correct user associated with the specified account.
 * @param htmlVariables a list of key-value pairs representing variables that can be used to populate the HTML template of the email. This allows for dynamic content generation in the email body, enabling personalized and context-specific notifications based on the provided variables.
 * @param type the type of alert or notification being sent, which can be used to determine the appropriate email template to use. This field helps in categorizing the email and ensuring that the correct template is applied based on the nature of the alert or notification being sent.
 */
public record SendEmailNotificationCommand(
        AccountId accountId,
        List<Pair<String, String>> htmlVariables,
        String type
) {

    public SendEmailNotificationCommand {
        if (htmlVariables == null) {
            throw new IllegalArgumentException("HTML variables list cannot be null");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
    }
}
