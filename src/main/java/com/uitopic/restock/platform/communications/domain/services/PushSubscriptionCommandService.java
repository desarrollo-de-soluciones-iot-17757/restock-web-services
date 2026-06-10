package com.uitopic.restock.platform.communications.domain.services;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;
import com.uitopic.restock.platform.communications.domain.model.commands.RegisterPushSubscriptionCommand;

import java.util.Optional;

/**
 * Service interface for handling commands related to push subscriptions.
 * This service is responsible for processing commands that involve creating,
 * updating, or deleting push subscriptions for users.
 */
public interface PushSubscriptionCommandService {

    /**
     * Handles the RegisterPushSubscriptionCommand to register a new push subscription for a user.
     *
     * @param command the command object containing the details of the push subscription to register
     * @return an Optional containing the registered PushSubscription if successful, or an empty Optional if registration failed
     */
    Optional<PushSubscription> handle(RegisterPushSubscriptionCommand command);
}
