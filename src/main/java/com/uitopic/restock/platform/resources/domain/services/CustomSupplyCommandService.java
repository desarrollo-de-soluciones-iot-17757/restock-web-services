package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteCustomSupplyCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateCustomSupplyCommand;

import java.util.Optional;

/**
 * Command service contract for CustomSupply write operations.
 */
public interface CustomSupplyCommandService {

    /**
     * Creates a new custom supply.
     *
     * @param command command with the custom supply data
     * @return created custom supply
     */
    CustomSupply handle(CreateCustomSupplyCommand command);

    /**
     * Updates an existing custom supply.
     *
     * @param command command with the updated custom supply data
     * @return updated custom supply, or empty if it was not found
     */
    Optional<CustomSupply> handle(UpdateCustomSupplyCommand command);

    /**
     * Deletes an existing custom supply.
     *
     * @param command command with the custom supply identifier
     */
    void handle(DeleteCustomSupplyCommand command);
}