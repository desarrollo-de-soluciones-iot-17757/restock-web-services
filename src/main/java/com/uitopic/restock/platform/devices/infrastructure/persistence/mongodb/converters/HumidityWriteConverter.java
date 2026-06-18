package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Humidity;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class HumidityWriteConverter implements Converter<Humidity, Document> {

    @Override
    public Document convert(Humidity source) {
        if (source == null) return null;
        return new Document()
                .append("minPercentage", source.minPercentage())
                .append("maxPercentage", source.maxPercentage());
    }
}
