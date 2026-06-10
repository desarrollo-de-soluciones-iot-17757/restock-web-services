package com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.services;

import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.Template;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResendEmailBuilder {

    private ResendEmailBuilder() {
        // Private constructor to prevent instantiation
    }

    public static CreateEmailOptions createDiscrepancyEmail(String userDirection, List<Pair<String, String>> htmlVariables) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("CUSTOM_SUPPLY_NAME", htmlVariables);

        var discrepancyDetectedTemplate = Template.builder()
                .id("discrepancy_detected_email")
                .variables(variables)
                .build();

        return CreateEmailOptions.builder()
                .from("onboarding@onboarding.resend.dev")
                .to(userDirection)
                .subject("Restock - Discrepancy Detected")
                .template(discrepancyDetectedTemplate)
                .build();
    }
}
