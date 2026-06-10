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
     * @param subject the subject line of the email. This field is important for providing a clear and concise summary of the email's content, helping the recipient understand the purpose of the email at a glance.
     * @param htmlVariables a list of key-value pairs representing variables that can be used to populate the HTML template of the email. This allows for dynamic content generation in the email body, enabling personalized and context-specific emails based on the provided variables.
     * @param templateId the name of the email template to be used for generating the email content. This field is crucial for selecting the appropriate template that matches the context of the email being sent, ensuring that the email content is relevant and properly formatted.
     */
    void sendEmail(String to, String subject, List<Pair<String ,String>> htmlVariables, String templateId);
}
