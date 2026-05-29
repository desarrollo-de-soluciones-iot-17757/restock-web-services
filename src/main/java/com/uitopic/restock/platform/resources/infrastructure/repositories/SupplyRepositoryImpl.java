package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of the SupplyRepository interface that interacts with the MongoDB database to perform CRUD operations on Supply entities.
 * This class serves as the bridge between the domain layer and the data access layer, allowing for separation of concerns and adherence to the repository pattern.
 */
@Repository
public class SupplyRepositoryImpl implements SupplyRepository {

    // Dependency on the SupplyMongoRepository to perform database operations
    private final SupplyMongoRepository supplyMongoRepository;

    // Constructor to initialize the SupplyMongoRepository dependency
    public SupplyRepositoryImpl(SupplyMongoRepository supplyMongoRepository) {
        this.supplyMongoRepository = supplyMongoRepository;
    }

    /**
     * Retrieves all Supply entities from the data source.
     *
     * @return a list of all Supply entities available in the data source
     */
    @Override
    public List<Supply> findAll() {
        return supplyMongoRepository.findAll();
    }
}
