package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.StockComparisonTaskPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockComparisonTaskPersistenceRepository extends MongoRepository<StockComparisonTaskPersistenceEntity, String> {
}
