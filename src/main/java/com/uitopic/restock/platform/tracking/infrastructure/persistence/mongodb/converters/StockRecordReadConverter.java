package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a {@link Double} read from MongoDB into a {@link StockRecord} value object.
 *
 * <p>{@link StockRecord} is stored as a double primitive in MongoDB rather than a nested
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the value object during document reads.
 */
@ReadingConverter
public class StockRecordReadConverter implements Converter<Double, StockRecord> {

    /**
     * Converts a {@link Double} read from MongoDB into a {@link StockRecord} value object.
     *
     * @param source the double value read from MongoDB, may be {@code null}
     * @return a new {@link StockRecord} reconstructed from the double value, or {@code null} if source is {@code null}
     */
    @Override
    public StockRecord convert(Double source) {
        if (source == null) return null;
        return new StockRecord(source);
    }
}
