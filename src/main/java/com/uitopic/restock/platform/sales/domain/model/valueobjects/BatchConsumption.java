package com.uitopic.restock.platform.sales.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

public record BatchConsumption(
        String batchId,
        String customSupplyId,
        Double quantityToConsume,
        UnitMeasurement unitMeasurement
) {
    public BatchConsumption {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom Supply ID cannot be null or blank");
        }
        if (quantityToConsume == null || quantityToConsume <= 0) {
            throw new IllegalArgumentException("Quantity to consume must be greater than zero");
        }
    }
}
