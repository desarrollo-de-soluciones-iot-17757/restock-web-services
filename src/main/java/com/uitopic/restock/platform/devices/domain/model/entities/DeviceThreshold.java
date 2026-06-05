package com.uitopic.restock.platform.devices.domain.model.entities;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
public class DeviceThreshold {

    private String id;
    private AccountId accountId;
    private String customSupplyId;
    private Double minStock;
    private Double maxStock;
    private Double anomalyThreshold;
    private Temperature temperature;
    private Humidity humidity;

    public DeviceThreshold(
            AccountId accountId,
            String customSupplyId,
            Double minStock,
            Double maxStock,
            Double anomalyThreshold,
            Temperature temperature,
            Humidity humidity
    ) {
        if (accountId == null)
            throw new IllegalArgumentException("Account ID cannot be null");
        if (customSupplyId == null || customSupplyId.isBlank())
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        if (minStock == null || minStock < 0)
            throw new IllegalArgumentException("Min stock cannot be null or negative");
        if (maxStock == null || maxStock <= 0)
            throw new IllegalArgumentException("Max stock must be greater than zero");
        if (minStock >= maxStock)
            throw new IllegalArgumentException("Min stock must be less than max stock");
        if (anomalyThreshold == null || anomalyThreshold < 0)
            throw new IllegalArgumentException("Anomaly threshold cannot be null or negative");

        this.accountId = accountId;
        this.customSupplyId = customSupplyId;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.anomalyThreshold = anomalyThreshold;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public void updateStockThresholds(Double minStock, Double maxStock, Double anomalyThreshold) {
        if (minStock == null || minStock < 0)
            throw new IllegalArgumentException("Min stock cannot be null or negative");
        if (maxStock == null || maxStock <= 0)
            throw new IllegalArgumentException("Max stock must be greater than zero");
        if (minStock >= maxStock)
            throw new IllegalArgumentException("Min stock must be less than max stock");
        if (anomalyThreshold == null || anomalyThreshold < 0)
            throw new IllegalArgumentException("Anomaly threshold cannot be null or negative");

        this.minStock = minStock;
        this.maxStock = maxStock;
        this.anomalyThreshold = anomalyThreshold;
    }

    public void updateEnvironmentalThresholds(Temperature temperature, Humidity humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
