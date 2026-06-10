package com.uitopic.restock.platform.communications.interfaces.rest.transform;

import com.uitopic.restock.platform.communications.domain.model.commands.RegisterPushSubscriptionCommand;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.RegisterPushSubscriptionResource;

/**
 * Assembler class responsible for converting a RegisterPushSubscriptionResource into a RegisterPushSubscriptionCommand.
 * This is used to transform incoming REST API requests into command objects that can be processed by the application layer.
 */
public class RegisterPushSubscriptionCommandFromResourceAssembler {

    /**
     * Converts a RegisterPushSubscriptionResource into a RegisterPushSubscriptionCommand.
     *
     * @param resource The resource object containing the data from the REST API request.
     * @return A RegisterPushSubscriptionCommand object that can be used by the application layer to register a new push subscription.
     */
    public static RegisterPushSubscriptionCommand toCommandFromResource(RegisterPushSubscriptionResource resource) {
        return new RegisterPushSubscriptionCommand(
                resource.userId(),
                resource.providerToken(),
                normalize(resource.clientPlatform()),
                normalize(resource.provider())
        );
    }

    /**
     * Normalizes a string value by trimming whitespace and converting it to uppercase.
     * This is useful for ensuring consistent formatting of platform and provider values.
     *
     * @param value The string value to normalize.
     * @return The normalized string, or null if the input value is null.
     */
    private static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
