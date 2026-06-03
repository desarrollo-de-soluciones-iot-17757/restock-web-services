package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.configuration.converters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link Money} value object into a {@link String} for MongoDB storage.
 *
 * <p>Stores money as a compact string in the format {@code "amount currencyCode"}
 * (e.g., {@code "19.99 PEN"}) instead of an embedded subdocument, keeping the
 * schema flat and query-friendly.
 *
 * @see MoneyReadConverter
 */
@WritingConverter
public class MoneyWriteConverter implements Converter<Money, String> {

    @Override
    public String convert(Money source) {
        if (source == null) return null;
        return source.getAmount().toPlainString() + " " + source.getCurrencyCode();
    }
}
