package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing NotificationPersistenceEntity objects in MongoDB.
 * This repository provides CRUD operations for notifications stored in the database.
 */
@Repository
public interface NotificationPersistenceRepository extends MongoRepository<NotificationPersistenceEntity, String> {

    /**
     * Finds all notifications for a given recipient user ID.
     *
     * @param recipientUserId the ID of the recipient user
     * @return a list of NotificationPersistenceEntity objects associated with the recipient user ID
     */
    List<NotificationPersistenceEntity> findByRecipientId(String recipientUserId);

}
