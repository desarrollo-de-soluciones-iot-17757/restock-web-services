package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.CompareStockCommand;

import java.util.Optional;

public interface StockComparisonTaskCommandService {

    Optional<StockComparisonTask> handle(CompareStockCommand command);
}
