package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.configuration.converters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a {@link String} read from MongoDB into a {@link UnitMeasurement} value object.
 *
 * <p>{@link UnitMeasurement} is stored as a plain string (the unit name) rather than a subdocument.
 *
 * @see UnitMeasurementWriteConverter
 */
@ReadingConverter
public class UnitMeasurementReadConverter implements Converter<String, UnitMeasurement> {

    @Override
    public UnitMeasurement convert(String source) {
        return source == null ? null : new UnitMeasurement(source);
    }
}
