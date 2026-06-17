package com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.services;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.ResendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ResendService interface for sending emails using the Resend email service. This class provides the functionality to send different types of alert emails based on the specified alert type and the provided HTML variables.
 * The sendEmail method evaluates the alert type and constructs the appropriate email options using the ResendEmailBuilder utility class, then attempts to send the email using the Resend email client. It also includes error handling to log any exceptions that occur during the email sending process.
 */
@Service
@Slf4j
public class ResendServiceImpl implements ResendService {

    /**
     * The API key for authenticating with the Resend email service, injected from the application configuration. This key is essential for initializing the Resend client and authorizing email sending operations.
     */
    @Value("${resend.api.key}")
    String resendApiKey;

    /**
     * @inheritDocs
     */
    @Override
    public void sendEmail(String to, List<Pair<String, String>> htmlVariables, String alertType) {

        // Initialize the Resend email client with the API key.
        Resend resend = new Resend(resendApiKey);

        // Initialize the CreateEmailOptions object to null. This will be populated based on the alert type.
        CreateEmailOptions params = null;

        // Evaluate the alert type and build the email options accordingly.
        switch (alertType) {
            case "DISCREPANCY": // DISCREPANCY alert type for notifications about discrepancies in inventory comparisons
                params = ResendEmailBuilder.createDiscrepancyEmail(to, htmlVariables);
                break;
            case "INVENTORY": // INVENTORY alert type for inventory-related notifications about stocks and batches
                params = ResendEmailBuilder.createInventoryEmail(to, htmlVariables);
                break;
            case "DEVICE": // DEVICE alert type for device-health related notifications

                break;
            default: // Default case for unsupported alert types, logging a warning message
                log.warn("Unsupported alert type: {}", alertType);
                break;
        }

        // If params is null, it means the alert type was unsupported, and it should not be sent as an email.
        if (params == null) return;

        // Attempt to send the email using the Resend email service.
        try {
            CreateEmailResponse response = resend.emails().send(params);
            log.info("Email sent successfully: {}", response.getId());
        } catch (ResendException e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
