package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ComparisonResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter that transforms a ComparisonResult value object into a JSON string for storage in MongoDB. This converter is registered with Spring Data MongoDB to automatically serialize the ComparisonResult during document writes. It handles null values gracefully and throws a RuntimeException if serialization fails, ensuring that any issues with data integrity are surfaced during the write process.
 */
@WritingConverter
public class ComparisonResultWriteConverter implements Converter<ComparisonResult, String> {

    /**
     * Converts a ComparisonResult value object into a JSON string for storage in MongoDB.
     *
     * @param source the ComparisonResult value object to convert, may be null
     * @return a JSON string representation of the ComparisonResult, or null if source is null
     */
    @Override
    public String convert(ComparisonResult source) {
        if (source == null) return null;
        try {
            return source.name();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ComparisonResult", e);
        }
    }
}
