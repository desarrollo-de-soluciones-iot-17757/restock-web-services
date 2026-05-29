package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoDB repository interface for managing Supply entities.
 * This interface extends the Spring Data MongoRepository, providing CRUD operations for Supply entities in a MongoDB database.
 */
@Repository
public interface SupplyMongoRepository extends MongoRepository<Supply, String> {

}
