package com.uitopic.restock.platform.analytics.application.internal.eventhandlers;

import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.model.events.AlertCreatedEvent;
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
 * Event handler for AlertCreatedEvent.
 * Registers a notifications metric when an alert is created.
 */
@Slf4j
@Component
public class AlertCreatedEventHandler {

    private final MetricCommandService metricCommandService;

    public AlertCreatedEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }

    /**
     * Handles the AlertCreatedEvent by registering a NOTIFICATIONS_RECEIVED metric.
     *
     * @param event the alert created event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AlertCreatedEvent event) {
        log.info("Processing AlertCreatedEvent for alert='{}'", event.alertId());

        var today = LocalDate.now();
        var dateRange = new DateRange(today, today);

        var command = new RegisterMetricCommand(
                MetricCategory.NOTIFICATIONS,
                MetricType.NOTIFICATIONS_RECEIVED,
                dateRange,
                event.accountId()
        );
        metricCommandService.handle(command);
    }
}
