package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter that transforms a {@link StockRecord} value object into a {@link Double} for storage in MongoDB.
 */
@WritingConverter
public class StockRecordWriteConverter implements Converter<StockRecord, Double> {

    /**
     * Converts a {@link StockRecord} value object into a {@link Double} for storage in MongoDB.
     *
     * @param source the {@link StockRecord} to convert, may be {@code null}
     * @return the stock level as a {@link Double} extracted from the {@link StockRecord}, or {@code null} if source is {@code null}
     */
    @Override
    public Double convert(StockRecord source) {
        if (source == null) return null;
        return source.getStock();
    }
}
