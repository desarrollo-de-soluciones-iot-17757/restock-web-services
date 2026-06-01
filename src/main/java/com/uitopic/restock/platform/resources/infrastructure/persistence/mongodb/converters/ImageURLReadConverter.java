package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converts a JSON {@link String} read from MongoDB into an {@link ImageURL} value object.
 *
 * <p>{@link ImageURL} is stored as a JSON string primitive in MongoDB rather than a nested
 * document. This converter is registered with Spring Data MongoDB to transparently reconstruct
 * the value object during document reads.
 *
 * @see ImageURLWriteConverter
 */
@ReadingConverter
public class ImageURLReadConverter implements Converter<String, ImageURL> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Converts the given JSON string from MongoDB into an {@link ImageURL} value object.
     *
     * @param source the JSON string read from MongoDB, may be {@code null}
     * @return a new {@link ImageURL} reconstructed from the JSON, or {@code null} if source is {@code null}
     */
    @Override
    public ImageURL convert(String source) {
        if (source == null) return null;
        try {
            return MAPPER.readValue(source, ImageURL.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ImageURL", e);
        }
    }
}
