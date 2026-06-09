package com.uitopic.restock.platform.devices.domain.model.valueobjects;

public record Temperature(Double minCelsius, Double maxCelsius) {

    public Temperature {
        if (minCelsius == null || maxCelsius == null)
            throw new IllegalArgumentException("Temperature bounds cannot be null");
        if (minCelsius >= maxCelsius)
            throw new IllegalArgumentException("Min temperature must be less than max temperature");
    }
}
