package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.MinimumStock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a {@link String} read from MongoDB into a {@link MinimumStock} value object.
 *
 * <p>Expects the stored value to be in the format {@code "minimumStock unitMeasurement"}
 * (e.g., {@code "10.0 kg"}), produced by {@link MinimumStockWriteConverter}.
 *
 * @see MinimumStockWriteConverter
 */
@ReadingConverter
public class MinimumStockReadConverter implements Converter<String, MinimumStock> {

    @Override
    public MinimumStock convert(String source) {
        if (source == null) return null;
        String[] parts = source.split(" ", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid MinimumStock format in MongoDB: '" + source + "'");
        }
        return new MinimumStock(Double.valueOf(parts[0]), parts[1]);
    }
}
