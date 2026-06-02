package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a {@link Double} value read from MongoDB into a {@link SupplyContent} value object.
 *
 * <p>{@link SupplyContent} is stored as a plain double primitive in MongoDB rather than an embedded
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the value object during document reads.
 *
 * @see SupplyContentWriteConverter
 */
@ReadingConverter
public class SupplyContentReadConverter implements Converter<Double, SupplyContent> {

    /**
     * Converts the given double from MongoDB into a {@link SupplyContent} value object.
     *
     * @param source the raw double value read from MongoDB, may be {@code null}
     * @return a new {@link SupplyContent} wrapping the source value, or {@code null} if source is {@code null}
     */
    @Override
    public SupplyContent convert(Double source) {
        if (source == null) return null;
        return new SupplyContent(source);
    }
}
