package com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.services;

import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Utility class for building email options for different email templates using the Resend email service.
 */
public final class ResendEmailBuilder {

    private ResendEmailBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Adds variables to the email template based on the provided list of key-value pairs and variable names.
     *
     * @param htmlVariables A list of key-value pairs representing the variables to be added to the email template.
     * @param variableNames A list of variable names that need to be extracted from the htmlVariables and added to the email template.
     * @param variables A map that will be populated with the variable names and their corresponding values extracted from the htmlVariables list. The variable names are used as keys, and the values are extracted based on matching keys in the htmlVariables list. If a variable name is not found in the htmlVariables, it defaults to "N/A".
     */
    private static void addVariable(
            List<Pair<String, String>> htmlVariables,
            List<String> variableNames,
            Map<String, Object> variables
    ) {
        // Create a set of allowed variable names for quick lookup
        Set<String> allowedNames = new HashSet<>(variableNames);

        // Iterate through the htmlVariables list and add the variables to the map if their keys match the allowed variable names
        htmlVariables.stream()
                .filter(pair -> allowedNames.contains(pair.getKey()))
                .forEach(pair -> variables.put(pair.getKey(), pair.getValue()));
    }

    /**
     * Builds the email options for the discrepancy detected email template.
     *
     * @param userDirection The recipient email address.
     * @param htmlVariables A list of key-value pairs to be used as variables in the email template.
     * @return CreateEmailOptions object containing the email configuration for the discrepancy detected email.
     */
    public static CreateEmailOptions createDiscrepancyEmail(
            String userDirection,
            List<Pair<String, String>> htmlVariables
    ) {
        // Template id for the discrepancy detected email template in Resend
        String templateId = "c67e6aeb-7dae-4b6b-98de-4a52e243f63c";

        // Initialize a map to hold the variables for the email template
        Map<String, Object> variables = new HashMap<>();

        // List of variable names that we want to extract from the htmlVariables and add to the email template
        List<String> variableNames = List.of(
                "CUSTOM_SUPPLY_NAME",
                "DEVICE_ID",
                "PHYSICAL_STOCK",
                "SYSTEM_STOCK",
                "THRESHOLD_CONFIGURED"
        );

        // Populate the variables map with the values extracted from the htmlVariables list based on the variable names
        addVariable(htmlVariables, variableNames, variables);

        // Build the email template using the template ID and the variables map
        var discrepancyDetectedTemplate = Template.builder()
                .id(templateId)
                .variables(variables)
                .build();

        // Build and return the CreateEmailOptions object with the email configuration, including the sender, recipient, subject, and the email template
        return CreateEmailOptions.builder()
                .from("onboarding@onboarding.resend.dev")
                .to(userDirection)
                .subject("Restock - Discrepancy Detected")
                .template(discrepancyDetectedTemplate)
                .build();
    }

    /**
     * Builds the email options for the inventory stock event detected email template.
     * It can be used for Zero Stock Event, Low Stock Event, and Overstock Event email templates since they share the same variables and template structure.
     *
     * @param userDirection The recipient email address.
     * @param htmlVariables A list of key-value pairs to be used as variables in the email template.
     * @return CreateEmailOptions object containing the email configuration for the inventory stock event detected email.
     */
    public static CreateEmailOptions createInventoryEmail(
            String userDirection,
            List<Pair<String, String>> htmlVariables
    ) {
        // Template id for the inventory stock event detected email template in Resend
        String templateId = "e0e57b93-6281-48dd-9155-e0616850fa7b";

        // Initialize a map to hold the variables for the email template
        Map<String, Object> variables = new HashMap<>();

        // List of variable names that we want to extract from the htmlVariables and add to the email template
        List<String> variableNames = List.of(
                "BRANCH_NAME",
                "CUSTOM_SUPPLY_NAME",
                "MINIMUM_STOCK",
                "STOCK_EVENT_TYPE",
                "CURRENT_STOCK",
                "UNIT_MEASUREMENT"
        );

        // Populate the variables map with the values extracted from the htmlVariables list based on the variable names
        addVariable(htmlVariables, variableNames, variables);

        // Build the email template using the template ID and the variables map
        var inventoryEventDetectedTemplate = Template.builder()
                .id(templateId)
                .variables(variables)
                .build();

        // Build and return the CreateEmailOptions object with the email configuration, including the sender, recipient, subject, and the email template
        return CreateEmailOptions.builder()
                .from("onboarding@onboarding.resend.dev")
                .to(userDirection)
                .subject("Restock - Inventory Event Detected")
                .template(inventoryEventDetectedTemplate)
                .build();
    }
}
