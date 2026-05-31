package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a {@link String} value read from MongoDB into an {@link InventoryState} enum value.
 *
 * <p>{@link InventoryState} is stored as a plain string in MongoDB rather than an embedded
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the enum during document reads. Case-insensitive fallback matching is applied when the stored
 * string does not exactly match an enum constant name.
 *
 * @see InventoryStateWriteConverter
 */
@ReadingConverter
public class InventoryStateReadConverter implements Converter<String, InventoryState> {

    /**
     * Converts the given string from MongoDB into an {@link InventoryState} enum value.
     * Attempts an exact match first, then falls back to case-insensitive comparison.
     *
     * @param source the raw string value read from MongoDB, may be {@code null}
     * @return the matching {@link InventoryState} constant, or {@code null} if source is {@code null}
     * @throws IllegalArgumentException if the source string does not match any {@link InventoryState} constant
     */
    @Override
    public InventoryState convert(String source) {
        if (source == null) return null;
        try {
            return InventoryState.valueOf(source);
        } catch (IllegalArgumentException ex) {
            for (InventoryState s : InventoryState.values()) {
                if (s.name().equalsIgnoreCase(source)) return s;
            }
            throw ex;
        }
    }
}
