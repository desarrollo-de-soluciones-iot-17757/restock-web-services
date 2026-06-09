package com.uitopic.restock.platform.resources.infrastructure.adapters;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers.SupplyPersistenceAssembler;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SupplyRepository interface that interacts with the MongoDB persistence layer.
 * This class serves as an adapter between the domain layer and the persistence layer, allowing for separation of concerns and adherence to the repository pattern.
 */
@Repository
public class SupplyRepositoryImpl implements SupplyRepository {

    // Reference to the persistence repository for Supply entities
    private final SupplyPersistenceRepository supplyPersistenceRepository;

    // Constructor injection of the persistence repository
    public SupplyRepositoryImpl(SupplyPersistenceRepository supplyPersistenceRepository) {
        this.supplyPersistenceRepository = supplyPersistenceRepository;
    }

    /**
     * Saves a supply to the repository.
     *
     * @param supply the supply to save
     * @return the saved supply
     */
    @Override
    public Supply save(Supply supply) {
        var saved = supplyPersistenceRepository.save(SupplyPersistenceAssembler.toPersistenceFromDomain(supply));
        return SupplyPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds all supplies in the repository.
     *
     * @return an Optional containing the supply if found, or empty if not found
     */
    @Override
    public List<Supply> findAll() {
        return supplyPersistenceRepository.findAll().stream()
                .map(SupplyPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Finds a supply by its unique identifier.
     *
     * @param id the unique identifier of the supply
     * @return an Optional containing the supply if found, or empty if not found
     */
    @Override
    public Optional<Supply> findById(String id) {
        return supplyPersistenceRepository.findById(id)
                .map(SupplyPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds a supply by its name.
     *
     * @param name supply name
     * @return supply if found
     */
    @Override
    public Optional<Supply> findByName(String name) {
        return supplyPersistenceRepository.findByName(name)
                .map(SupplyPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Checks whether a supply with the given name exists.
     *
     * @param name supply name
     * @return true if the supply exists
     */
    @Override
    public boolean existsByName(String name) {
        return supplyPersistenceRepository.existsByName(name);
    }
}
