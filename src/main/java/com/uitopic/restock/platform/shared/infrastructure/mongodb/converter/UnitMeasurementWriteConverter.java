package com.uitopic.restock.platform.shared.infrastructure.mongodb.converter;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link UnitMeasurement} value object into a {@link String} for MongoDB storage.
 *
 * <p>Stores the unit name as a plain string primitive instead of an embedded document.
 *
 * @see UnitMeasurementReadConverter
 */
@WritingConverter
public class UnitMeasurementWriteConverter implements Converter<UnitMeasurement, String> {

    @Override
    public String convert(UnitMeasurement source) {
        return source == null ? null : source.unitName();
    }
}
