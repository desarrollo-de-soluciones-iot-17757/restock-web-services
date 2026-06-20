package com.uitopic.restock.platform.sales.application.internal.eventhandlers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.sales.domain.model.events.SalesOrderCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Handles SalesOrderCompletedEvent after a sales order is marked as completed.
 * Recovers the aggregate state and communicates with the Resources context via ACL.
 */
@Slf4j
@Component
public class SalesOrderCompletedEventHandler {

    private final ExternalResourcesService externalResourcesService;


    /**
     * Constructor dependency injection.
     *
     * @param externalResourcesService the outbound ACL service for resources
     */
    public SalesOrderCompletedEventHandler(ExternalResourcesService externalResourcesService) {
        this.externalResourcesService = externalResourcesService;
    }

    /**
     * Processes stock reduction in the Resources context for each item in the completed sales order.
     *
     * @param event sales order completed event
     */
    @EventListener
    public void on(SalesOrderCompletedEvent event) {
        log.info("Event Handler: Processing physical stock deduction for Completed Order ID: {}", event.salesOrderId());

        try {
            event.batchConsumptions().forEach(consumption -> {
                log.debug("Deducting {} from batchId={}",
                        consumption.quantityToConsume(), consumption.batchId());
                externalResourcesService.subtractBatchStock(
                        event.branchId(),
                        consumption.batchId(),
                        consumption.quantityToConsume()
                );
            });

            log.info("Event Handler: Physical stock successfully deducted for Order ID: {}", event.salesOrderId());

        } catch (Exception e) {
            log.error("Event Handler: Critical error executing async stock deduction for Order ID {}: {}",
                    event.salesOrderId(), e.getMessage());
        }
    }
}