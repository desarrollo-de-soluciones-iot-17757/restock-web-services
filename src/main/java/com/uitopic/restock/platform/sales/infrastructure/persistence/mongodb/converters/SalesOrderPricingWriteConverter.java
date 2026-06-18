package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderPricing;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a SalesOrderPricing value object into a JSON String for MongoDB storage.
 */
@WritingConverter
public class SalesOrderPricingWriteConverter implements Converter<SalesOrderPricing, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convert(SalesOrderPricing source) {
        if (source == null) return null;
        try {
            return MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize SalesOrderPricing", e);
        }
    }
}