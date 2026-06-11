package com.uitopic.restock.platform.communications.application.internal.outboundservices.emailprovider;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Interface for sending emails. This service abstracts the email sending functionality, allowing for different implementations (e.g., SMTP, third-party email services).
 */
public interface EmailService {

    /**
     * Sends an email to the specified recipient with the given subject and HTML variables, using a specified template.
     *
     * @param to the email address of the recipient. This field is essential for ensuring that the email is sent to the correct recipient and is a fundamental part of the email sending process.
     * @param htmlVariables a list of key-value pairs representing variables that can be used to populate the HTML template of the email. This allows for dynamic content generation in the email body, enabling personalized and context-specific emails based on the provided variables.
     * @param alertType the type of alert or notification being sent, which can be used to determine the appropriate email template to use. This field helps in categorizing the email and ensuring that the correct template is applied based on the nature of the alert or notification being sent.
     */
    void sendEmail(String to, List<Pair<String ,String>> htmlVariables, String alertType);
}
