package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.configuration.converter;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * Converter to read AccountId objects from MongoDB string values.
 * This converter transforms a string representation of an account ID into an AccountId value object.
 */
@Component
@ReadingConverter
public class AccountIdReadConverter implements Converter<String, AccountId> {

    /**
     * Converts a string into an AccountId object.
     *
     * @param source the string representation of the account ID
     * @return an AccountId object constructed from the input string
     */
    @Override
    public AccountId convert(String source) {
        return new AccountId(source);
    }
}
