package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.SubscriptionPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubscriptionPersistenceRepository extends MongoRepository<SubscriptionPersistenceEntity, String> {
    Optional<SubscriptionPersistenceEntity> findByAccountId(AccountId accountId);
    Optional<SubscriptionPersistenceEntity> findByStripeSubscriptionId(String stripeSubscriptionId);
}
