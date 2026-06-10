package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.StockComparisonTaskPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing stock comparison tasks in MongoDB.
 * This repository provides CRUD operations for StockComparisonTaskPersistenceEntity.
 */
@Repository
public interface StockComparisonTaskPersistenceRepository extends MongoRepository<StockComparisonTaskPersistenceEntity, String> {
}
