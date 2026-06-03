package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.SupplyPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB repository for Supply documents.
 */
@Repository
public interface SupplyPersistenceRepository extends MongoRepository<SupplyPersistenceEntity, String> {

    /**
     * Finds a supply by its name.
     *
     * @param name supply name
     * @return supply if found
     */
    Optional<SupplyPersistenceEntity> findByName(String name);

    /**
     * Checks whether a supply with the given name exists.
     *
     * @param name supply name
     * @return true if the supply exists
     */
    boolean existsByName(String name);
}