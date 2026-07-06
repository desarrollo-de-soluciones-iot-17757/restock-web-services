package com.uitopic.restock.platform.sales.application.internal.commandservices;

import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalPlanningService;
import com.uitopic.restock.platform.sales.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.sales.domain.exceptions.SalesOrderNotFoundException;
import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.commands.*;
import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.domain.services.SalesOrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Application service implementation for SalesOrder write operations.
 * Orchestrates domain aggregate behaviors and persistence.
 */
@Slf4j
@Service
public class SalesOrderCommandServiceImpl implements SalesOrderCommandService {

    private final SalesOrderRepository salesOrderRepository;
    private final ExternalResourcesService externalResourcesService;
    private final ExternalPlanningService externalPlanningService;

    public SalesOrderCommandServiceImpl(
            SalesOrderRepository salesOrderRepository,
            ExternalResourcesService externalResourcesService,
            ExternalPlanningService externalPlanningService) {
        this.salesOrderRepository = salesOrderRepository;
        this.externalResourcesService = externalResourcesService;
        this.externalPlanningService = externalPlanningService;
    }

    @Override
    public SalesOrder handle(CreateSalesOrderCommand command) {
        SalesOrder salesOrder = new SalesOrder(
                command.accountId(),
                command.branchId()
        );
        return salesOrderRepository.save(salesOrder);
    }

    @Override
    public SalesOrder handle(AddProductToOrderCommand command) {
        log.info("Adding product {} to sales order {}", command.productId(), command.orderId());
        var orderIdAsObjectId = new org.bson.types.ObjectId(command.orderId());

        SalesOrder salesOrder = salesOrderRepository.findById(orderIdAsObjectId.toHexString())
                .orElseThrow(() -> new SalesOrderNotFoundException("Sales order not found: " + command.orderId()));
        var createItemCommand = new CreateSalesOrderItemCommand(
                command.productId(),
                command.productType().toString(),
                command.nameSnapshot(),
                command.unitPrice(),
                command.currency(),
                command.quantity()
        );
        salesOrder.addItem(new SalesOrderItem(createItemCommand));
        return salesOrderRepository.save(salesOrder);
    }

    @Override
    public SalesOrder handle(RemoveProductFromOrderCommand command) {
        log.info("Removing item {} from sales order {}", command.itemId(), command.orderId());
        SalesOrder salesOrder = salesOrderRepository.findById(command.orderId())
                .orElseThrow(() -> new SalesOrderNotFoundException("Sales order not found: " + command.orderId()));
        salesOrder.removeItem(command.itemId());
        return salesOrderRepository.save(salesOrder);
    }

    @Override
    public void handle(CancelSalesOrderCommand command) {
        log.info("Cancelling sales order {}", command.orderId());

        SalesOrder salesOrder = salesOrderRepository.findById(command.orderId())
                .orElseThrow(() -> new SalesOrderNotFoundException("Sales order not found: " + command.orderId()));

        salesOrder.cancel();

        salesOrderRepository.save(salesOrder);
        log.info("Sales order {} successfully cancelled", command.orderId());
    }

    @Override
    public SalesOrder handle(CompleteSalesOrderCommand command) {
        SalesOrder salesOrder = salesOrderRepository.findById(command.salesOrderId())
                .orElseThrow(() -> new IllegalArgumentException("..."));

        String branchId = salesOrder.getBranchId();

        for (SalesOrderItem item : salesOrder.getItems()) {
            List<BatchConsumption> consumptions = new ArrayList<>();

            if (item.getProductType() == ProductType.SUPPLY) {
                // A SUPPLY item is a custom supply sold directly (not part of a
                // kit/recipe "bill of materials"), so productId IS the
                // customSupplyId itself — there's no ingredients list to look up.
                Double totalQuantityNeeded = (double) item.getQuantity();
                BatchConsumption batchConsumption = externalResourcesService.resolveBatchConsumption(
                        branchId,
                        item.getProductId(),
                        totalQuantityNeeded
                );

                externalResourcesService.subtractBatchStock(branchId, batchConsumption.batchId(), totalQuantityNeeded);

                consumptions.add(batchConsumption);
            } else {
                List<ExternalPlanningService.IngredientRequirement> ingredients =
                        externalPlanningService.getProductIngredients(item.getProductId());

                for (ExternalPlanningService.IngredientRequirement ingredient : ingredients) {
                    Double totalQuantityNeeded = ingredient.quantityPerUnit() * item.getQuantity();
                    BatchConsumption batchConsumption = externalResourcesService.resolveBatchConsumption(
                            branchId,
                            ingredient.customSupplyId(),
                            totalQuantityNeeded
                    );

                    externalResourcesService.subtractBatchStock(branchId, batchConsumption.batchId(), totalQuantityNeeded);

                    consumptions.add(batchConsumption);
                }
            }

            if (!consumptions.isEmpty()) {
                salesOrder.assignBatchConsumptions(item.getId(), consumptions);
            }
        }
        salesOrder.complete();
        return salesOrderRepository.save(salesOrder);
    }
}