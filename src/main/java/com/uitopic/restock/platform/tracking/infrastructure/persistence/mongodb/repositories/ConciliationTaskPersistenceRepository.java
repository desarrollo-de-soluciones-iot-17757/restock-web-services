package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.ConciliationTaskPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConciliationTaskPersistenceRepository extends MongoRepository<ConciliationTaskPersistenceEntity, String> {

}
