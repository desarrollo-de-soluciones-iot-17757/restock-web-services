package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationPersistenceRepository extends MongoRepository<NotificationPersistenceEntity, String> {
}
