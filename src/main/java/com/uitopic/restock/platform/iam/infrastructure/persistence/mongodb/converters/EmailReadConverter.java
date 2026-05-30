package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * MongoDB read converter for the {@link Email} value object.
 * Deserializes a plain {@link String} from the database back into
 * an {@link Email} value object.
 */
@ReadingConverter
public class EmailReadConverter implements Converter<String, Email> {

    @Override
    public Email convert(String source) {
        return new Email(source);
    }
}
