package com.uitopic.restock.platform.analytics.application.internal.eventhandlers;

import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.model.events.CustomSupplyCreatedEvent;
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
 * Event handler for CustomSupplyCreatedEvent.
 * Registers an inventory metric when a custom supply is created.
 */
@Slf4j
@Component
public class CustomSupplyCreatedEventHandler {

    private final MetricCommandService metricCommandService;

    public CustomSupplyCreatedEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }

    /**
     * Handles the CustomSupplyCreatedEvent by registering a SUPPLIES_CREATED metric.
     *
     * @param event the custom supply created event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CustomSupplyCreatedEvent event) {
        log.info("Processing CustomSupplyCreatedEvent for supply='{}'", event.customSupplyId());

        var today = LocalDate.now();
        var dateRange = new DateRange(today, today);

        var command = new RegisterMetricCommand(
                MetricCategory.INVENTORY,
                MetricType.SUPPLIES_CREATED,
                dateRange,
                event.accountId()
        );
        metricCommandService.handle(command);
    }
}
