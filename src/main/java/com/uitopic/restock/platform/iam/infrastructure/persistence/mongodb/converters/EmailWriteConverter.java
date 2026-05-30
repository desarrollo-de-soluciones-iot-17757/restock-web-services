package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * MongoDB write converter for the {@link Email} value object.
 * Serializes an {@link Email} instance to a plain {@link String},
 * so it is stored as a primitive value in the database instead of
 * an embedded document.
 */
@WritingConverter
public class EmailWriteConverter implements Converter<Email, String> {

    @Override
    public String convert(Email source) {
        return source.email();
    }
}
