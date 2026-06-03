package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities.SalesOrderPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderPersistenceRepository extends MongoRepository<SalesOrderPersistenceEntity, String> {
}
