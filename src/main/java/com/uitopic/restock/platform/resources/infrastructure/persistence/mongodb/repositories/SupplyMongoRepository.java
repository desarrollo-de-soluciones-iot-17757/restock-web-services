package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoDB repository for {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply}
 * entities within the resources bounded context.
 */
@Repository
public interface SupplyMongoRepository extends MongoRepository<Supply, String> {

}
