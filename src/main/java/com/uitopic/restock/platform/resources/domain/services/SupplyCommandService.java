package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.commands.SeedSuppliesCommand;
import com.uitopic.restock.platform.resources.domain.model.entities.Supply;

import java.util.Optional;

public interface SupplyCommandService {
    Supply handle(SeedSuppliesCommand command);
    Optional<Supply> update(String id, SeedSuppliesCommand command);
    void delete(String id);
}
