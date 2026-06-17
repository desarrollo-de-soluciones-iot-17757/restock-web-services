package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * MongoDB reading converter that maps a String to a MetricCategory enum.
 */
@ReadingConverter
public class MetricCategoryReadConverter implements Converter<String, MetricCategory> {

    /**
     * Converts a String source to a MetricCategory enum value.
     *
     * @param source the source string to convert
     * @return the corresponding MetricCategory enum, or null if the source is null
     */
    @Override
    public MetricCategory convert(String source) {
        return source == null ? null : MetricCategory.valueOf(source);
    }
}
