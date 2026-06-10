package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.HumidityRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter that transforms a {@link HumidityRecord} value object into a {@link Double} representing the humidity percentage for storage in MongoDB.
 */
@WritingConverter
public class HumidityRecordWriteConverter implements Converter<HumidityRecord, Double> {

    /**
     * Converts a {@link HumidityRecord} value object into a {@link Double} representing the humidity percentage for storage in MongoDB.
     *
     * @param source the {@link HumidityRecord} to convert, may be {@code null}
     * @return the humidity percentage as a {@link Double}, or {@code null} if source is {@code null}
     */
    @Override
    public Double convert(HumidityRecord source) {
        if (source == null) return null;
        return source.getHumidityPercentage();
    }
}
