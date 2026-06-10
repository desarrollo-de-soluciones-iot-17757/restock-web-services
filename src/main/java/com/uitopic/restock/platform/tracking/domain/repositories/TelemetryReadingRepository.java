package com.uitopic.restock.platform.tracking.domain.repositories;

import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;

/**
 * Repository interface for managing telemetry readings, providing methods for saving and retrieving telemetry data related to inventory discrepancies. This interface defines the contract for persisting telemetry reading entities, allowing for the storage and retrieval of telemetry data for tracking inventory discrepancies in real-time.
 */
public interface TelemetryReadingRepository {

    /**
     * Saves a telemetry reading to the repository, allowing for the storage and retrieval of telemetry data for tracking inventory discrepancies. This method is responsible for persisting the telemetry reading entity, which contains information about stock levels, temperature, humidity, and the timestamp of the reading.
     *
     * @param telemetryReading the telemetry reading entity to be saved, containing the physical stock, temperature, humidity, timestamp, and device ID, provided by the request
     * @return the saved telemetry reading entity, which may include additional information such as a generated ID or timestamps after being persisted in the repository
     */
    TelemetryReading save(TelemetryReading telemetryReading);
}
