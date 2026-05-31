package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;

import java.util.Optional;

/**
 * Domain service interface defining the command contract for {@link CustomSupply} aggregate operations.
 *
 * <p>Declares the write-side operations available on custom supplies: creation, update,
 * and deletion. Implementations live in the application layer
 * ({@link com.uitopic.restock.platform.resources.application.internal.commandservices.CustomSupplyCommandServiceImpl}).
 */
public interface CustomSupplyCommandService {

    /**
     * Handles the creation of a new custom supply for the account specified in the command.
     * Validates that the supply name is unique within the account and that the referenced
     * supply template exists.
     *
     * @param command the command containing all data required to create the custom supply
     * @return the newly created and persisted {@link CustomSupply} aggregate
     * @throws org.springframework.web.server.ResponseStatusException with 409 if the name already exists,
     *         or 422 if the referenced supply template is not found
     */
    CustomSupply handle(CreateCustomSupplyCommand command);

    /**
     * Updates an existing custom supply with the data provided in the command.
     * Replaces all mutable fields; the supply name and category may also be changed.
     *
     * @param id      the unique identifier of the custom supply to update
     * @param command the command containing the updated supply data
     * @return an {@link Optional} containing the updated {@link CustomSupply}, or empty if not found
     */
    Optional<CustomSupply> update(String id, CreateCustomSupplyCommand command);

    /**
     * Deletes a custom supply by its unique identifier.
     * Deletion is blocked if the supply has active batches with remaining stock.
     * Publishes a {@link com.uitopic.restock.platform.resources.domain.model.events.CustomSupplyDeletedEvent}
     * upon successful deletion.
     *
     * @param id the unique identifier of the custom supply to delete
     * @throws org.springframework.web.server.ResponseStatusException with 404 if not found,
     *         or 409 if active batches with stock exist
     */
    void delete(String id);
}
