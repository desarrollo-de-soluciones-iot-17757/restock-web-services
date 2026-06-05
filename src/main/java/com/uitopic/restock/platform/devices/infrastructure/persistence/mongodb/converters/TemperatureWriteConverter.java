package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class TemperatureWriteConverter implements Converter<Temperature, String> {

    @Override
    public String convert(Temperature source) {
        if (source == null) return null;
        return source.minCelsius() + ":" + source.maxCelsius();
    }
}
