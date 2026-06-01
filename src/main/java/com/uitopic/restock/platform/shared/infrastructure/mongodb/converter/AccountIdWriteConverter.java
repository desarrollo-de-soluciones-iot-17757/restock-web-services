package com.uitopic.restock.platform.shared.infrastructure.mongodb.converter;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

/**
 * Converter to write AccountId objects to MongoDB.
 * This converter transforms an AccountId value object into a String for storage in MongoDB.
 */
@Component
@WritingConverter
public class AccountIdWriteConverter implements Converter<AccountId, String> {

    /**
     * Converts an AccountId object into a String for MongoDB storage.
     *
     * @param source the AccountId object to be converted
     * @return the account ID as a String extracted from the AccountId object
     */
    @Override
    public String convert(AccountId source) {
        return source.accountId();
    }
}