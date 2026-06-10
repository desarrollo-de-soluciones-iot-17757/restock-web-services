package com.uitopic.restock.platform.communications.infrastructure.adapters;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;
import com.uitopic.restock.platform.communications.domain.repositories.PushSubscriptionRepository;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.assemblers.PushSubscriptionPersistenceAssembler;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories.PushSubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the PushSubscriptionRepository interface using MongoDB for persistence.
 * This class serves as an adapter between the domain model and the MongoDB persistence layer.
 */
@Repository
public class PushSubscriptionRepositoryImpl implements PushSubscriptionRepository {

    /** MongoDB repository for managing push subscriptions. */
    private final PushSubscriptionPersistenceRepository pushSubscriptionMongoRepository;

    /**
     *  Constructs a PushSubscriptionRepositoryImpl with the specified PushSubscriptionPersistenceRepository.
     *
     * @param pushSubscriptionMongoRepository the MongoDB repository for push subscriptions
     */
    public PushSubscriptionRepositoryImpl(PushSubscriptionPersistenceRepository pushSubscriptionMongoRepository) {
        this.pushSubscriptionMongoRepository = pushSubscriptionMongoRepository;
    }

    /** Saves a push subscription to the repository.
     *
     * @param pushSubscription the push subscription to save
     * @return the saved push subscription with any generated fields (e.g., ID)
     *
     *
     */
    @Override
    public PushSubscription save(PushSubscription pushSubscription) {
        var saved = pushSubscriptionMongoRepository.save(PushSubscriptionPersistenceAssembler.toPersistenceFromDomain(pushSubscription));
        return PushSubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a push subscription by its provider token.
     * @param providerToken the provider token to search for
     * @return an Optional containing the found push subscription, or empty if not found
     */
    @Override
    public Optional<PushSubscription> findByProviderToken(String providerToken) {
        return pushSubscriptionMongoRepository.findByProviderToken(providerToken)
                .map(PushSubscriptionPersistenceAssembler::toDomainFromPersistence);
    }


    @Override
    public List<PushSubscription> findByUserIdAndActiveTrueOrderByUpdatedAtDesc(String userId) {
        return pushSubscriptionMongoRepository
                .findByUserIdAndActiveTrueOrderByUpdatedAtDesc(userId)
                .stream()
                .map(PushSubscriptionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
