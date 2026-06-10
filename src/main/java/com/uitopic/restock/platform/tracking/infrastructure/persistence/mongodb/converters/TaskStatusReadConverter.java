package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TaskStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter that transforms a string read from MongoDB into a TaskStatus enum value.
 */
@ReadingConverter
public class TaskStatusReadConverter implements Converter<String, TaskStatus> {

    /**
     * Converts a string read from MongoDB into a TaskStatus enum value.
     *
     * @param source the string read from MongoDB, may be null
     * @return the corresponding TaskStatus enum value, or null if source is null
     */
    @Override
    public TaskStatus convert(String source) {
        if (source == null) return null;
        try {
            return TaskStatus.valueOf(source);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert TaskStatus: invalid value " + source, e);
        }
    }
}
