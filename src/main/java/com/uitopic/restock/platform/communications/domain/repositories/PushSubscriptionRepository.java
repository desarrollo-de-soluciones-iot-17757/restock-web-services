package com.uitopic.restock.platform.communications.domain.repositories;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing push subscriptions in the communications domain.
 *
 * This repository provides methods for saving and retrieving push subscription entities,
 * which represent the subscription details for sending push notifications to users.
 */
public interface PushSubscriptionRepository {

    /**
     * Saves a push subscription to the data store.
     *
     * @param pushSubscription The push subscription entity to be saved.
     * @return The saved push subscription entity, potentially with an assigned ID or other generated fields.
     */
    PushSubscription save(PushSubscription pushSubscription);

    /**
     * Finds a push subscription by its provider token.
     *
     * @param providerToken The unique token provided by the push notification provider.
     * @return An Optional containing the push subscription if found, or empty if not found.
     */
    Optional<PushSubscription> findByProviderToken(String providerToken);

    /**
     * Finds all active push subscriptions for a given user, ordered by the last update time in descending order.
     *
     * @param userId The unique identifier of the user.
     * @return A list of active push subscriptions associated with the given user, ordered by updatedAt in descending order.
     */
    List<PushSubscription> findByUserIdAndActiveTrueOrderByUpdatedAtDesc(String userId); // ← agregar

}
