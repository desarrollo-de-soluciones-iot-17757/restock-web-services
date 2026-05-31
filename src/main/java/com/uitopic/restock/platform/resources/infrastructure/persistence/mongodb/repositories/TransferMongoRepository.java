package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoDB repository interface for managing InventoryTransfer entities. This repository provides CRUD operations and query methods for handling inventory transfer records in the MongoDB database.
 */
@Repository
public interface TransferMongoRepository extends MongoRepository<InventoryTransfer, String> {
}
