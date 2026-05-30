package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a Stock value object to an Integer for MongoDB storage. This converter is used when writing data to the database.
 * It extracts the integer value from the Stock object using its accessor method.
 */
public class StockWriteConverter implements Converter<Stock, Integer> {

    @Override
    public Integer convert(Stock source) {
        if (source == null) return null;
        return source.stock();
    }
}

