package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class TemperatureReadConverter implements Converter<String, Temperature> {

    @Override
    public Temperature convert(String source) {
        if (source == null || source.isBlank()) return null;
        String[] parts = source.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid Temperature format: " + source);
        return new Temperature(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }
}
