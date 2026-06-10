package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TaskStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converts a {@link TaskStatus} value object into its string representation for storage in MongoDB.
 */
@WritingConverter
public class TaskStatusWriteConverter implements Converter<TaskStatus, String> {

    /**
     * Converts a {@link TaskStatus} value object into its string representation for storage in MongoDB.
     *
     * @param source the {@link TaskStatus} to convert, may be {@code null}
     * @return the string representation of the {@link TaskStatus} for MongoDB, or {@code null} if source is {@code null}
     */
    @Override
    public String convert(TaskStatus source) {
        if (source == null) return null;
        return source.name();
    }
}
