package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String from MongoDB to InventoryState when reading from the database.
 * Uses InventoryState.valueOf(...) by default (expects the stored string to match enum name).
 */
public class InventoryStateReadConverter implements Converter<String, InventoryState> {

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

