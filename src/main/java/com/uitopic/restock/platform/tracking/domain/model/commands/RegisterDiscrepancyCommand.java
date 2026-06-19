package com.uitopic.restock.platform.tracking.domain.model.commands;

import com.uitopic.restock.platform.tracking.domain.exceptions.DiscrepancyResolutionException;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;

/**
 * Command representing the registration of a discrepancy from a stock comparison task.
 *
 * @param stockComparisonTask the stock comparison task that originated the discrepancy
 * @param riskLevel the risk level assigned to the discrepancy
 */
public record RegisterDiscrepancyCommand(
        StockComparisonTask stockComparisonTask,
        DiscrepancyAlertLevel riskLevel
) {

    public RegisterDiscrepancyCommand {
        if (stockComparisonTask == null) {
            throw new DiscrepancyResolutionException("Stock comparison task cannot be null");
        }

        if (riskLevel == null) {
            throw new DiscrepancyResolutionException("Risk level cannot be null");
        }
    }
}