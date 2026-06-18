package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class TemperatureReadConverter implements Converter<Document, Temperature> {

    @Override
    public Temperature convert(Document source) {
        if (source == null) return null;
        return new Temperature(
                source.getDouble("minCelsius"),
                source.getDouble("maxCelsius")
        );
    }
}
