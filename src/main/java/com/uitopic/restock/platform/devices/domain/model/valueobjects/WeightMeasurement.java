package com.uitopic.restock.platform.devices.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

import java.time.LocalDate;

public record WeightMeasurement(
        Double netWeight,
        Double tareWeight,
        Double grossWeight,
        LocalDate calibrationDate,
        UnitMeasurement weightUnit
) {
    public WeightMeasurement {
        if (netWeight == null || netWeight < 0)
            throw new IllegalArgumentException("Net weight cannot be null or negative");
        if (tareWeight == null || tareWeight < 0)
            throw new IllegalArgumentException("Tare weight cannot be null or negative");
        if (grossWeight == null || grossWeight < 0)
            throw new IllegalArgumentException("Gross weight cannot be null or negative");
        if (calibrationDate == null)
            throw new IllegalArgumentException("Calibration date cannot be null");
        if (weightUnit == null)
            throw new IllegalArgumentException("Weight unit cannot be null");
    }
}
