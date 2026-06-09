package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class HumidityReadConverter implements Converter<String, Humidity> {

    @Override
    public Humidity convert(String source) {
        if (source == null || source.isBlank()) return null;
        String[] parts = source.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid Humidity format: " + source);
        return new Humidity(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }
}
