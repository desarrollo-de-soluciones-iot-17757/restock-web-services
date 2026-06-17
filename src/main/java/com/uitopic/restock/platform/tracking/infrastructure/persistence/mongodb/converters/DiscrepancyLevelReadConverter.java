package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter that transforms a JSON string read from MongoDB into a DiscrepancyLevel value object. This converter is registered with Spring Data MongoDB to automatically reconstruct the DiscrepancyLevel during document reads. It handles null values gracefully and throws a RuntimeException if deserialization fails, ensuring that any issues with data integrity are surfaced during the read process.
 */
@ReadingConverter
public class DiscrepancyLevelReadConverter implements Converter<String, DiscrepancyAlertLevel> {

    /**
     * Converts a JSON string from MongoDB into a DiscrepancyLevel value object.
     *
     * @param source the JSON string read from MongoDB, may be null
     * @return a new DiscrepancyLevel reconstructed from the JSON, or null if source is null
     */
    @Override
    public DiscrepancyAlertLevel convert(String source) {
        if (source == null) return null;
        try {
            return DiscrepancyAlertLevel.valueOf(source);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize DiscrepancyLevel", e);
        }
    }
}
