package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.Temperature;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class TemperatureWriteConverter implements Converter<Temperature, Document> {

    @Override
    public Document convert(Temperature source) {
        if (source == null) return null;
        return new Document()
                .append("minCelsius", source.minCelsius())
                .append("maxCelsius", source.maxCelsius());
    }
}
