package com.uitopic.restock.platform.devices.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

import java.time.LocalDate;

public record WeightMeasurement(
        Double unitStockWeight,
        Double tareWeight,
        Double grossWeight,
        LocalDate calibrationDate,
        UnitMeasurement weightUnit
) {
    public WeightMeasurement {
        if (unitStockWeight == null || unitStockWeight <= 0)
            throw new IllegalArgumentException("Unit stock weight must be greater than zero");
        if (tareWeight == null || tareWeight < 0)
            throw new IllegalArgumentException("Tare weight cannot be null or negative");
        if (grossWeight != null && grossWeight < 0)
            throw new IllegalArgumentException("Gross weight cannot be negative");
        if (weightUnit == null)
            throw new IllegalArgumentException("Weight unit cannot be null");
    }

    public Double netWeight() {
        return unitStockWeight;
    }
}
