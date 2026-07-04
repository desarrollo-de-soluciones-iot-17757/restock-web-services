package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.AccountPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountPersistenceRepository extends MongoRepository<AccountPersistenceEntity, String> {
    Optional<AccountPersistenceEntity> findByAccountId(AccountId accountId);
    Optional<AccountPersistenceEntity> findByStripeCustomerId(String stripeCustomerId);
}
