package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class MacAddressReadConverter implements Converter<String, MacAddress> {

    @Override
    public MacAddress convert(String source) {
        return source == null ? null : new MacAddress(source);
    }
}
