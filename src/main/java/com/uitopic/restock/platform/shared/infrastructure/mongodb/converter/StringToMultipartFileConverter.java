package com.uitopic.restock.platform.shared.infrastructure.mongodb.converter;


import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StringToMultipartFileConverter implements Converter<String, MultipartFile> {

    @Override
    public MultipartFile convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        throw new IllegalArgumentException("Cannot convert non-empty string to MultipartFile");
    }
}
