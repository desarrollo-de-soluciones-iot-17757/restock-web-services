package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.commands.RegisterDiscrepancyCommand;

/**
 * Service interface for handling commands related to inventory discrepancies.
 * This service is responsible for processing commands that register discrepancies in inventory tracking.
 */
public interface DiscrepancyCommandService {

    /**
     * Handles the registration of a discrepancy by processing the provided command.
     *
     * @param command the command containing the details of the discrepancy to be registered
     */
    void handle(RegisterDiscrepancyCommand command);
}
