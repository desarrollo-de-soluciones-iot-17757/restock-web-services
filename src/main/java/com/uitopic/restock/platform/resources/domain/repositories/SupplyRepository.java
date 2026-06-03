package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Supply entities.
 */
public interface SupplyRepository {

    /**
     * Saves a supply to the repository.
     *
     * @param supply the supply to save
     * @return the saved supply
     */
    Supply save(Supply supply);

    /**
     * Finds all supplies in the repository.
     *
     * @return an Optional containing the supply if found, or empty if not found
     */
    List<Supply> findAll();

    /**
     * Finds a supply by its unique identifier.
     *
     * @param id the unique identifier of the supply
     * @return an Optional containing the supply if found, or empty if not found
     */
    Optional<Supply> findById(String id);

    /**
     * Finds a supply by its name.
     *
     * @param name supply name
     * @return supply if found
     */
    Optional<Supply> findByName(String name);

    /**
     * Checks whether a supply with the given name exists.
     *
     * @param name supply name
     * @return true if the supply exists
     */
    boolean existsByName(String name);
}
