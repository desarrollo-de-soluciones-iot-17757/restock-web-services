package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;

/**
 * Command used to create a pending conciliation task from a critical discrepancy.
 *
 * @param discrepancy critical discrepancy that requires conciliation
 */
public record CreateConciliationTaskCommand(
        Discrepancy discrepancy
) {
    public CreateConciliationTaskCommand {
        if (discrepancy == null) {
            throw new IllegalArgumentException("Discrepancy cannot be null");
        }
    }
}
