package com.uitopic.restock.platform.devices.domain.model.valueobjects;

public record Humidity(Double minPercentage, Double maxPercentage) {

    public Humidity {
        if (minPercentage == null || maxPercentage == null)
            throw new IllegalArgumentException("Humidity bounds cannot be null");
        if (minPercentage < 0 || maxPercentage > 100)
            throw new IllegalArgumentException("Humidity percentage must be between 0 and 100");
        if (minPercentage >= maxPercentage)
            throw new IllegalArgumentException("Min humidity must be less than max humidity");
    }
}
