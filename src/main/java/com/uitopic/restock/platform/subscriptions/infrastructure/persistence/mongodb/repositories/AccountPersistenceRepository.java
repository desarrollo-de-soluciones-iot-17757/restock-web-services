package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.AccountPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPersistenceRepository extends MongoRepository<AccountPersistenceEntity, String> {
}
