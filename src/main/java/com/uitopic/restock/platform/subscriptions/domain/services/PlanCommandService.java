package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.commands.SeedSubscriptionPlansCommand;

/**
 * Command service contract for subscription plan write operations.
 */
public interface PlanCommandService {

    /**
     * Initializes or updates Restock's predefined subscription plans.
     *
     * @param command command that starts the seeding process
     */
    void handle(SeedSubscriptionPlansCommand command);
}
