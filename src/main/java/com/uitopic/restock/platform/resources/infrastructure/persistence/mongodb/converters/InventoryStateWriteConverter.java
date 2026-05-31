package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts an {@link InventoryState} enum value into a {@link String} for storage in MongoDB.
 *
 * <p>Stores the enum constant name as a plain string primitive instead of an embedded document,
 * keeping the MongoDB schema compact and human-readable. This converter is registered with
 * Spring Data MongoDB to transparently serialize the enum during document writes.
 *
 * @see InventoryStateReadConverter
 */
@WritingConverter
public class InventoryStateWriteConverter implements Converter<InventoryState, String> {

    /**
     * Converts the given {@link InventoryState} enum value into its string name for MongoDB storage.
     *
     * @param source the {@link InventoryState} to convert, may be {@code null}
     * @return the enum constant name as a {@link String}, or {@code null} if source is {@code null}
     */
    @Override
    public String convert(InventoryState source) {
        if (source == null) return null;
        return source.name();
    }
}
