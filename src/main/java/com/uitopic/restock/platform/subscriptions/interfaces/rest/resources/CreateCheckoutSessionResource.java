package com.uitopic.restock.platform.subscriptions.interfaces.rest.resources;

public record CreateCheckoutSessionResource(
        String accountId,
        String planId
) {
}
