package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts an {@link ImageURL} value object into a JSON {@link String} for storage in MongoDB.
 *
 * <p>Stores the image URL and its Cloudinary public ID as a JSON string primitive instead of
 * a nested document.
 *
 * @see ImageURLReadConverter
 */
@WritingConverter
public class ImageURLWriteConverter implements Converter<ImageURL, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Converts the given {@link ImageURL} value object into a JSON string for MongoDB storage.
     *
     * @param source the {@link ImageURL} value object to convert, may be {@code null}
     * @return a JSON {@link String} representing the image URL, or {@code null} if source is {@code null}
     */
    @Override
    public String convert(ImageURL source) {
        if (source == null) return null;
        try {
            return MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ImageURL", e);
        }
    }
}
