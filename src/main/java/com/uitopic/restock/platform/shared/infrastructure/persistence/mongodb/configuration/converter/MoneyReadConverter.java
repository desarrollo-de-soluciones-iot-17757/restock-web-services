package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.configuration.converter;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;

/**
 * Converts a {@link String} read from MongoDB into a {@link Money} value object.
 *
 * <p>Expects the stored value to be in the format {@code "amount currencyCode"}
 * (e.g., {@code "19.99 PEN"}), produced by {@link MoneyWriteConverter}.
 *
 * @see MoneyWriteConverter
 */
@ReadingConverter
public class MoneyReadConverter implements Converter<String, Money> {

    @Override
    public Money convert(String source) {
        if (source == null) return null;
        String[] parts = source.split(" ", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Money format in MongoDB: '" + source + "'");
        }
        return new Money(new BigDecimal(parts[0]), parts[1]);
    }
}
