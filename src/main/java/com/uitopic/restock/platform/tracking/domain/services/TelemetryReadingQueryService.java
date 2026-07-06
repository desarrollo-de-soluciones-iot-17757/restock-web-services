package com.uitopic.restock.platform.tracking.domain.services;

import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetTelemetryReadingsByDeviceIdQuery;

import java.util.List;

/**
 * Service interface for handling queries related to telemetry readings.
 */
public interface TelemetryReadingQueryService {

    /**
     * Handles the query to retrieve telemetry readings by device ID.
     *
     * @param query the query containing the device ID
     * @return list of matching telemetry readings
     */
    List<TelemetryReading> handle(GetTelemetryReadingsByDeviceIdQuery query);
}
