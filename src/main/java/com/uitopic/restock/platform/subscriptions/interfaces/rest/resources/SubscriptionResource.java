package com.uitopic.restock.platform.subscriptions.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SubscriptionResource",
        description = "Represents a subscription resource in the REST API."
)
public record SubscriptionResource() {

    /**
     * Validates the subscription resource. This method can be used to ensure that the resource is in a valid state before processing it further.
     */
    public SubscriptionResource {

    }
}
