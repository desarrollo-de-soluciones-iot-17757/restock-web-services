package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TemperatureRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter that transforms a {@link TemperatureRecord} value object into a {@link Double} representing the temperature in Celsius for storage in MongoDB.
 */
@WritingConverter
public class TemperatureRecordWriteConverter implements Converter<TemperatureRecord, Double> {

    /**
     * Converts a {@link TemperatureRecord} value object into a {@link Double} representing the temperature in Celsius for storage in MongoDB.
     *
     * @param source the {@link TemperatureRecord} to convert, may be {@code null}
     * @return the temperature in Celsius as a {@link Double}, or {@code null} if source is {@code null}
     */
    @Override
    public Double convert(TemperatureRecord source) {
        if (source == null) return null;
        return source.getTemperatureInCelsius();
    }
}
