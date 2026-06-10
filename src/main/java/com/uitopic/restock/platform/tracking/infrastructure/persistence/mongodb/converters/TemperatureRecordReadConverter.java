package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TemperatureRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter to read a Double value from MongoDB and convert it into a TemperatureRecord value object.
 */
@ReadingConverter
public class TemperatureRecordReadConverter implements Converter<Double, TemperatureRecord> {

    /**
     * Converts a Double read from MongoDB into a TemperatureRecord value object.
     *
     * @param source the Double value read from MongoDB, may be {@code null}
     * @return a new TemperatureRecord constructed from the Double, or {@code null} if source is {@code null}
     */
    @Override
    public TemperatureRecord convert(Double source) {
        if (source == null) return null;
        return new TemperatureRecord(source);
    }
}
