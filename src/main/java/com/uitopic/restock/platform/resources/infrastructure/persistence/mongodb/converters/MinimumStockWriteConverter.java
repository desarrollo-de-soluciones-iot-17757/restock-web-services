package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link MinimumStock} value object into a {@link String} for MongoDB storage.
 *
 * <p>Stores the minimum stock as a compact string in the format
 * {@code "minimumStock unitMeasurement"} (e.g., {@code "10.0 kg"}) instead of
 * an embedded subdocument.
 *
 * @see MinimumStockReadConverter
 */
@WritingConverter
public class MinimumStockWriteConverter implements Converter<MinimumStock, String> {

    @Override
    public String convert(MinimumStock source) {
        if (source == null) return null;
        return source.getMinimumStock() + " " + source.getUnitMeasurement();
    }
}
