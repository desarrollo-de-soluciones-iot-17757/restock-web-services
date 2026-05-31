package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts an {@link Integer} value read from MongoDB into a {@link Stock} value object.
 *
 * <p>{@link Stock} is stored as a plain integer primitive in MongoDB rather than an embedded
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the value object during document reads.
 *
 * @see StockWriteConverter
 */
@ReadingConverter
public class StockReadConverter implements Converter<Integer, Stock> {

    /**
     * Converts the given integer from MongoDB into a {@link Stock} value object.
     *
     * @param source the raw integer value read from MongoDB, may be {@code null}
     * @return a new {@link Stock} wrapping the source value, or {@code null} if source is {@code null}
     */
    @Override
    public Stock convert(Integer source) {
        if (source == null) return null;
        return new Stock(source);
    }
}
