package com.uitopic.restock.platform.analytics.application.internal.eventhandlers;

import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.model.events.SaleCreatedEvent;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;
import com.uitopic.restock.platform.analytics.domain.services.MetricCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

/**
 * Event handler for SaleCreatedEvent.
 * Registers a sales metric when a sale is created.
 */
@Slf4j
@Component
public class SaleCreatedEventHandler {

    private final MetricCommandService metricCommandService;

    public SaleCreatedEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }

    /**
     * Handles the SaleCreatedEvent by registering a SALES_MADE metric.
     *
     * @param event the sale created event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(SaleCreatedEvent event) {
        log.info("Processing SaleCreatedEvent for sale='{}'", event.saleId());

        var today = LocalDate.now();
        var dateRange = new DateRange(today, today);

        var command = new RegisterMetricCommand(
                MetricCategory.SALES,
                MetricType.SALES_MADE,
                dateRange,
                event.accountId()
        );
        metricCommandService.handle(command);
    }
}
