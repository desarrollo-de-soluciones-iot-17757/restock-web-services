package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts an {@link Address} value object into a JSON {@link String} for storage in MongoDB.
 *
 * <p>Stores only the four primitive fields (address, city, regionOrState, country)
 * as a JSON string primitive instead of a nested document, explicitly excluding
 * derived getters such as {@code getFullAddress()}.
 *
 * @see AddressReadConverter
 */
@WritingConverter
public class AddressWriteConverter implements Converter<Address, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(MapperFeature.USE_GETTERS_AS_SETTERS)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    /**
     * Converts the given {@link Address} value object into a JSON string for MongoDB storage.
     *
     * @param source the {@link Address} value object to convert, may be {@code null}
     * @return a JSON {@link String} representing the address, or {@code null} if source is {@code null}
     */
    @Override
    public String convert(Address source) {
        if (source == null) return null;
        try {
            return MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Address", e);
        }
    }
}