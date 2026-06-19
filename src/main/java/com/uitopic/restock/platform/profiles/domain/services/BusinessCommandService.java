package com.uitopic.restock.platform.profiles.domain.services;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.domain.model.commands.CreateBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.DeleteBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.UpdateBusinessCommand;

import java.util.Optional;

public interface BusinessCommandService {
    Business handle(CreateBusinessCommand command);
    Optional<Business> handle(UpdateBusinessCommand command);
    void handle(DeleteBusinessCommand command);
}
