package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link Stock} value object into an {@link Integer} for storage in MongoDB.
 *
 * <p>Stores the stock quantity as a plain integer primitive instead of an embedded document,
 * keeping the MongoDB schema compact and query-friendly. This converter is registered with
 * Spring Data MongoDB to transparently serialize the value object during document writes.
 *
 * @see StockReadConverter
 */
@WritingConverter
public class StockWriteConverter implements Converter<Stock, Double> {

    /**
     * Converts the given {@link Stock} value object into a raw double for MongoDB storage.
     *
     * @param source the {@link Stock} value object to convert, may be {@code null}
     * @return the raw stock quantity as a {@link Double}, or {@code null} if source is {@code null}
     */
    @Override
    public Double convert(Stock source) {
        if (source == null) return null;
        return source.stock();
    }
}
