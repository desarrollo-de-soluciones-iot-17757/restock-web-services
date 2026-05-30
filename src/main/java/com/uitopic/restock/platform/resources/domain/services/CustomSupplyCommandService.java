package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateCustomSupplyCommand;

import java.util.Optional;

public interface CustomSupplyCommandService {
    CustomSupply handle(CreateCustomSupplyCommand command);
    Optional<CustomSupply> update(String id, CreateCustomSupplyCommand command);
    void delete(String id);
}
