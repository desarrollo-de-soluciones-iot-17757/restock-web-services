package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderPricing;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a JSON String read from MongoDB into a SalesOrderPricing value object.
 */
@ReadingConverter
public class SalesOrderPricingReadConverter implements Converter<String, SalesOrderPricing> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public SalesOrderPricing convert(String source) {
        if (source == null) return null;
        try {
            return MAPPER.readValue(source, SalesOrderPricing.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize SalesOrderPricing", e);
        }
    }
}