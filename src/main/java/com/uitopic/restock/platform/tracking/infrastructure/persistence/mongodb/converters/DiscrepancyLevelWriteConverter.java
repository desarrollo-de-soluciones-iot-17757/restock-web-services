package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyLevel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter that transforms a DiscrepancyLevel value object into a JSON string for storage in MongoDB. This converter is registered with Spring Data MongoDB to automatically serialize the DiscrepancyLevel during document writes. It handles null values gracefully and throws a RuntimeException if serialization fails, ensuring that any issues with data integrity are surfaced during the write process.
 */
@WritingConverter
public class DiscrepancyLevelWriteConverter implements Converter<DiscrepancyLevel, String> {

    /**
     * Converts a DiscrepancyLevel value object into a JSON string for storage in MongoDB.
     *
     * @param source the DiscrepancyLevel value object to convert, may be null
     * @return a JSON string representation of the DiscrepancyLevel, or null if source is null
     */
    @Override
    public String convert(DiscrepancyLevel source) {
        if (source == null) return null;
        try {
            return source.name();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize DiscrepancyLevel", e);
        }
    }
}
