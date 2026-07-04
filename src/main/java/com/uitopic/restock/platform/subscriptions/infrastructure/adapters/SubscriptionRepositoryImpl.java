package com.uitopic.restock.platform.subscriptions.infrastructure.adapters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers.SubscriptionPersistenceAssembler;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionPersistenceRepository subscriptionPersistenceRepository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository subscriptionPersistenceRepository) {
        this.subscriptionPersistenceRepository = subscriptionPersistenceRepository;
    }

    @Override
    public Optional<Subscription> findById(String id) {
        return subscriptionPersistenceRepository.findById(id)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Subscription> findByAccountId(AccountId accountId) {
        return subscriptionPersistenceRepository.findByAccountId(accountId)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId) {
        return subscriptionPersistenceRepository.findByStripeSubscriptionId(stripeSubscriptionId)
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Subscription save(Subscription subscription) {
        var entity = SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription);
        var saved = subscriptionPersistenceRepository.save(entity);
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(String id) {
        subscriptionPersistenceRepository.deleteById(id);
    }
}
