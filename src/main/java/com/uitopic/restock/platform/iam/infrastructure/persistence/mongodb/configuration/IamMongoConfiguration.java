package com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.configuration;

import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailReadConverter;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

/**
 * MongoDB configuration for the IAM bounded context.
 * Registers custom converters so that the {@code Email} value object
 * is stored as a plain string in MongoDB instead of an embedded document.
 */
@Configuration
public class IamMongoConfiguration {

    @Bean
    public MongoCustomConversions iamMongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new EmailWriteConverter(),
                new EmailReadConverter()
        ));
    }
}
