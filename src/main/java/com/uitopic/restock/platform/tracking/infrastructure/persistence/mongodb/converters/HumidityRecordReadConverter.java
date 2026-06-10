package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.HumidityRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter that transforms a JSON string read from MongoDB into a HumidityRecord value object. This converter is registered with Spring Data MongoDB to automatically reconstruct the HumidityRecord during document reads. It handles null values gracefully and throws a RuntimeException if deserialization fails, ensuring that any issues with data integrity are surfaced during the read process.
 */
@ReadingConverter
public class HumidityRecordReadConverter implements Converter<Double, HumidityRecord> {

    /**
     * Converts a JSON string from MongoDB into a HumidityRecord value object.
     *
     * @param source the JSON string read from MongoDB, may be null
     * @return a new HumidityRecord reconstructed from the JSON, or null if source is null
     */
    @Override
    public HumidityRecord convert(Double source) {
        if (source == null) return null;
        try {
            return new HumidityRecord(source);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize HumidityRecord", e);
        }
    }
}
