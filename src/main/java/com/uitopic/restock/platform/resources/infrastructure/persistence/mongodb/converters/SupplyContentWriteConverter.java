package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.SupplyContent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link SupplyContent} value object into a {@link Double} for storage in MongoDB.
 *
 * <p>Stores the content quantity as a plain double primitive instead of an embedded document,
 * keeping the MongoDB schema compact and query-friendly. This converter is registered with
 * Spring Data MongoDB to transparently serialize the value object during document writes.
 *
 * @see SupplyContentReadConverter
 */
@WritingConverter
public class SupplyContentWriteConverter implements Converter<SupplyContent, Double> {

    /**
     * Converts the given {@link SupplyContent} value object into a raw double for MongoDB storage.
     *
     * @param source the {@link SupplyContent} value object to convert, may be {@code null}
     * @return the raw content quantity as a {@link Double}, or {@code null} if source is {@code null}
     */
    @Override
    public Double convert(SupplyContent source) {
        if (source == null) return null;
        return source.getContent();
    }
}
