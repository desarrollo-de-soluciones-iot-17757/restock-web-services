package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;

/**
 * Command used to close pending conciliation tasks when stock normalizes.
 *
 * @param stockComparisonTask stock comparison task used to find pending tasks
 */
public record ClosePendingConciliationTasksCommand(
        StockComparisonTask stockComparisonTask
) {
    public ClosePendingConciliationTasksCommand {
        if (stockComparisonTask == null) {
            throw new IllegalArgumentException("Stock comparison task cannot be null");
        }
    }
}
