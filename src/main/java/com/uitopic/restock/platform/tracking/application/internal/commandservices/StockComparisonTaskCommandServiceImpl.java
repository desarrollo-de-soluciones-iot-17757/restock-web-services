package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.CompareStockCommand;
import com.uitopic.restock.platform.tracking.domain.services.StockComparisonTaskCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class StockComparisonTaskCommandServiceImpl implements StockComparisonTaskCommandService {

    @Override
    @Transactional
    public Optional<StockComparisonTask> handle(CompareStockCommand command) {
        // Always decorate the method with a @Transactional annotation to ensure that any exceptions thrown during the execution of the method will trigger a rollback of the transaction, maintaining data integrity and consistency.

        return Optional.empty();
    }
}
