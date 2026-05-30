package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts an Integer to a Stock object when reading from MongoDB. This is used to map the integer value stored in the database to the Stock value object in the application.
 * If the source integer is null, it returns null; otherwise, it creates a new Stock instance with the integer value.
 */
public class StockReadConverter implements Converter<Integer, Stock> {

    @Override
    public Stock convert(Integer source) {
        return source == null ? null : new Stock(source);
    }
}

