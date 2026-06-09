package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class MacAddressWriteConverter implements Converter<MacAddress, String> {

    @Override
    public String convert(MacAddress source) {
        return source == null ? null : source.address();
    }
}
