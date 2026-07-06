package com.uitopic.restock.platform.tracking.application.internal.queryservices;

import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetTelemetryReadingsByDeviceIdQuery;
import com.uitopic.restock.platform.tracking.domain.repositories.TelemetryReadingRepository;
import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of TelemetryReadingQueryService for handling queries related to telemetry readings.
 */
@Service
@RequiredArgsConstructor
public class TelemetryReadingQueryServiceImpl implements TelemetryReadingQueryService {

    private final TelemetryReadingRepository telemetryReadingRepository;

    @Override
    public List<TelemetryReading> handle(GetTelemetryReadingsByDeviceIdQuery query) {
        return telemetryReadingRepository.findAllByDeviceId(query.deviceId());
    }
}
