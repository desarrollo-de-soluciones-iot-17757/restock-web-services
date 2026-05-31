package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a JSON {@link String} read from MongoDB into an {@link Address} value object.
 *
 * <p>{@link Address} is stored as a JSON string primitive in MongoDB rather than a nested
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the value object during document reads.
 *
 * <p>Unknown properties such as legacy {@code fullAddress} fields are ignored to ensure
 * backwards compatibility with previously persisted documents.
 *
 * @see AddressWriteConverter
 */
@ReadingConverter
public class AddressReadConverter implements Converter<String, Address> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Converts the given JSON string from MongoDB into an {@link Address} value object.
     *
     * @param source the JSON string read from MongoDB, may be {@code null}
     * @return a new {@link Address} reconstructed from the JSON, or {@code null} if source is {@code null}
     */
    @Override
    public Address convert(String source) {
        if (source == null) return null;
        try {
            return MAPPER.readValue(source, Address.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize Address", e);
        }
    }
}