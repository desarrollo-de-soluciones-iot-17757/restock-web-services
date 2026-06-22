package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.BatchConsumptionResource;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.IngredientResolvedResource;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.SalesOrderItemResource;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.SalesOrderResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static reactor.netty.http.HttpConnectionLiveness.log;

/**
 * Assembler that converts a {@link SalesOrder} aggregate into a {@link SalesOrderResource} DTO.
 */
public class SalesOrderResourceFromEntityAssembler {

    public static SalesOrderResource toResourceFromEntity(SalesOrder entity) {
        if(entity == null) return null;
        List<SalesOrderItemResource> itemResources = entity.getItems().stream()
                .map(item -> {

                    log.info("Inspecting item: Product={}, ID={}", item.getProductId(), item.getId());

                    Map<String, List<BatchConsumption>> consumptionsBySupply = item.getConsumedBatches().stream()
                            .collect(Collectors.groupingBy(BatchConsumption::customSupplyId));

                    List<IngredientResolvedResource> ingredientsResolved = new ArrayList<>();

                    consumptionsBySupply.forEach((supplyId, consumptions) -> {

                        Double totalRequired = consumptions.stream()
                                .mapToDouble(BatchConsumption::quantityToConsume)
                                .sum();

                        List<BatchConsumptionResource> batchesReserved = consumptions.stream()
                                .map(b -> new BatchConsumptionResource(
                                        b.batchId(),
                                        b.quantityToConsume()
                                )).toList();

                        ingredientsResolved.add(new IngredientResolvedResource(
                                supplyId,
                                item.getNameSnapshot(),
                                totalRequired,
                                batchesReserved
                        ));
                    });

                    return new SalesOrderItemResource(
                            item.getProductId(),
                            item.getQuantity(),
                            ingredientsResolved
                    );
                }).toList();
        Double totalAmount = entity.getPricing() != null ? entity.getPricing().total().getAmount().doubleValue() : 0.0;

        return new SalesOrderResource(
                entity.getId(),
                entity.getStatus() != null ? entity.getStatus().name() : "PENDING",
                itemResources,
                totalAmount
        );
    }
}