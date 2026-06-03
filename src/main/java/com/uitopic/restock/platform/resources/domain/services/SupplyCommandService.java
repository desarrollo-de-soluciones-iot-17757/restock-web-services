package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;

/**
 * Command service contract for Supply write operations.
 */
public interface SupplyCommandService {

    /**
     * Seeds the base supply catalog.
     *
     * @param command command to seed supplies
     */
    void handle(SeedSuppliesCommand command);
}