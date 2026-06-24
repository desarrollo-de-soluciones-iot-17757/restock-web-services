package com.uitopic.restock.platform.subscriptions.infrastructure.adapters;

import com.uitopic.restock.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers.SubscriptionPersistenceAssembler;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SubscriptionPersistenceRepository repository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(
                repository.save(SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription))
        );
    }

    @Override
    public Optional<Subscription> findByBusinessId(String businessId) {
        return repository.findByBusinessId(businessId)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId) {
        return repository.findByStripeReferenceSubscriptionId(stripeSubscriptionId)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }
}
