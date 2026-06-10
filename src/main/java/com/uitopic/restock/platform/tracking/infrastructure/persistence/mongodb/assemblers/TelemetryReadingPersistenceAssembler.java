package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.TelemetryReadingPersistenceEntity;

/**
 * Assembler class responsible for converting between TelemetryReading domain entities and TelemetryReadingPersistenceEntity objects used for MongoDB persistence. This class provides static methods to facilitate the mapping of fields between the two representations, allowing the application to work with domain entities while still being able to persist and retrieve data from MongoDB effectively.
 */
public final class TelemetryReadingPersistenceAssembler {

    private TelemetryReadingPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a TelemetryReadingPersistenceEntity from MongoDB to a TelemetryReading domain entity. This method maps the fields from the persistence entity to the corresponding fields in the domain entity, allowing the application to work with the data in its domain model form.
     *
     * @param entity the TelemetryReadingPersistenceEntity retrieved from MongoDB, containing the data for the telemetry reading, including physical stock, temperature, humidity, timestamp, and device ID
     * @return a TelemetryReading domain entity containing the data from the persistence entity, ready for use in the application's domain logic, with all fields mapped accordingly
     */
    public static TelemetryReading toDomainFromPersistence(TelemetryReadingPersistenceEntity entity) {
        if (entity == null) return null;

        var telemetryReading = new TelemetryReading();
        telemetryReading.setId(entity.getId());
        telemetryReading.setPhysicalStock(entity.getPhysicalStock());
        telemetryReading.setTemperatureInCelsius(entity.getTemperatureInCelsius());
        telemetryReading.setHumidityPercentage(entity.getHumidityPercentage());
        telemetryReading.setTimestamp(entity.getTimestamp());
        telemetryReading.setDeviceId(entity.getDeviceId());

        return telemetryReading;
    }

    /**
     * Converts a TelemetryReading domain entity to a TelemetryReadingPersistenceEntity for storage in MongoDB. If the TelemetryReading has a non-null ID, it will be set on the persistence entity; otherwise, MongoDB will generate a new ID when the entity is saved.
     *
     * @param telemetryReading the TelemetryReading domain entity to convert, containing the data for the telemetry reading, including physical stock, temperature, humidity, timestamp, and device ID
     * @return a TelemetryReadingPersistenceEntity containing the data from the TelemetryReading, ready for persistence in MongoDB, with the ID set if it is not null
     */
    public static TelemetryReadingPersistenceEntity toPersistenceFromDomain(TelemetryReading telemetryReading) {
        if (telemetryReading == null) return null;

        var entity = new TelemetryReadingPersistenceEntity();

        // Only set the ID if it is not null, to allow MongoDB to generate a new ID for new entities
        if (telemetryReading.getId() != null) {
            entity.setId(telemetryReading.getId());
        }
        entity.setPhysicalStock(telemetryReading.getPhysicalStock());
        entity.setTemperatureInCelsius(telemetryReading.getTemperatureInCelsius());
        entity.setHumidityPercentage(telemetryReading.getHumidityPercentage());
        entity.setTimestamp(telemetryReading.getTimestamp());
        entity.setDeviceId(telemetryReading.getDeviceId());

        return entity;
    }
}
