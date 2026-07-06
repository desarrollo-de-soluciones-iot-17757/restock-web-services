package com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.WeightMeasurement;

/**
 *  This interface defines the contract for the EdgeService, which is responsible for handling operations related to edge devices in the system. The EdgeService provides a method to register a device using its MAC address. This allows the system to keep track of connected edge devices and manage them accordingly. The implementation of this interface will handle the actual logic for registering devices and maintaining their information within the system.
 */
public interface EdgeService {

    void registerDevice(String macAddress, String deviceToken);

    void configureDevice(
            String macAddress,
            String assignedBatchId,
            Double minStock,
            Double maxStock,
            Double minTemperatureCelsius,
            Double maxTemperatureCelsius,
            Double minHumidityPercentage,
            Double maxHumidityPercentage,
            Double anomalyThreshold
    );

    void calibrateDevice(
            String macAddress,
            String assignedBatchId,
            Double minStock,
            Double maxStock,
            Double minTemperatureCelsius,
            Double maxTemperatureCelsius,
            Double minHumidityPercentage,
            Double maxHumidityPercentage,
            Double anomalyThreshold,
            WeightMeasurement weightMeasurement
    );
}
