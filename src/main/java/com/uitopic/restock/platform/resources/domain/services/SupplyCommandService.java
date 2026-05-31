package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;

import java.util.Optional;

/**
 * Domain service interface defining the command contract for
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply}
 * operations within the resources bounded context.
 */
public interface SupplyCommandService {
    Supply handle(SeedSuppliesCommand command);
    Optional<Supply> update(String id, SeedSuppliesCommand command);
    void delete(String id);
}
