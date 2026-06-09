package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities.MetricPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricPersistenceRepository extends MongoRepository<MetricPersistenceEntity, String> {
}
