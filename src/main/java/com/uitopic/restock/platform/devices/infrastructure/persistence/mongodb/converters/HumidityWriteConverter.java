package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class HumidityWriteConverter implements Converter<Humidity, String> {

    @Override
    public String convert(Humidity source) {
        if (source == null) return null;
        return source.minPercentage() + ":" + source.maxPercentage();
    }
}
