package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts an InventoryState to a String for MongoDB storage. By default, stores the enum name().
 */
public class InventoryStateWriteConverter implements Converter<InventoryState, String> {

    @Override
    public String convert(InventoryState source) {
        return source == null ? null : source.name();
    }
}
