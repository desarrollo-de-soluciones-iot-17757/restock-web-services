package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class HumidityReadConverter implements Converter<Document, Humidity> {

    @Override
    public Humidity convert(Document source) {
        if (source == null) return null;
        return new Humidity(
                source.getDouble("minPercentage"),
                source.getDouble("maxPercentage")
        );
    }
}
