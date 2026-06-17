package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.converters;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * MongoDB writing converter that maps a MetricCategory enum to a String.
 */
@WritingConverter
public class MetricCategoryWriteConverter implements Converter<MetricCategory, String> {

    /**
     * Converts a MetricCategory enum to a String for MongoDB storage.
     *
     * @param source the MetricCategory enum to convert
     * @return the string name of the enum, or null if the source is null
     */
    @Override
    public String convert(MetricCategory source) {
        return source == null ? null : source.name();
    }
}
