package com.uitopic.restock.platform.tracking.infrastructure.adapters;

import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.repositories.TelemetryReadingRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers.TelemetryReadingPersistenceAssembler;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.TelemetryReadingPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the TelemetryReadingRepository interface for MongoDB persistence.
 * This class uses the TelemetryReadingPersistenceRepository to perform CRUD operations on telemetry readings.
 */
@Repository
@RequiredArgsConstructor
public class TelemetryReadingRepositoryImpl implements TelemetryReadingRepository {

    // Repository for persisting telemetry readings in MongoDB
    private final TelemetryReadingPersistenceRepository telemetryReadingMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public TelemetryReading save(TelemetryReading telemetryReading) {
        var saved = telemetryReadingMongoRepository
                .save(TelemetryReadingPersistenceAssembler
                        .toPersistenceFromDomain(telemetryReading));

        return TelemetryReadingPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
