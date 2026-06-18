package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.PushSubscriptionPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing push subscription persistence in MongoDB.
 * This interface extends MongoRepository, providing CRUD operations for PushSubscriptionPersistenceEntity.
 */
@Repository
public interface PushSubscriptionPersistenceRepository extends MongoRepository<PushSubscriptionPersistenceEntity, String> {

    /**
     * Finds a push subscription by its provider token.
     *
     * @param providerToken the unique token provided by the push notification provider
     * @return the PushSubscriptionPersistenceEntity associated with the given provider token, or null if no such subscription exists
     */
    Optional<PushSubscriptionPersistenceEntity> findFirstByProviderTokenOrderByUpdatedAtDesc(String providerToken);

    /**
     * Finds all push subscriptions by provider token.
     *
     * @param providerToken the unique token provided by the push notification provider
     * @return all push subscriptions associated with the given provider token
     */
    List<PushSubscriptionPersistenceEntity> findAllByProviderToken(String providerToken);

    /**
     * Finds all active push subscriptions for a given user, ordered by the last update time in descending order.
     *
     * @param userId the unique identifier of the user
     * @return a list of active PushSubscriptionPersistenceEntity instances associated with the given user, ordered by updatedAt in descending order
     */
    List<PushSubscriptionPersistenceEntity> findByUserIdAndActiveTrueOrderByUpdatedAtDesc(String userId);

}
