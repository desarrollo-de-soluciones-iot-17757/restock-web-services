package com.uitopic.restock.platform.shared.infrastructure.mongodb.configuration;

import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailReadConverter;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailWriteConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.AddressReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.AddressWriteConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.ImageURLReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.ImageURLWriteConverter;
import com.uitopic.restock.platform.shared.infrastructure.mongodb.converter.AccountIdReadConverter;
import com.uitopic.restock.platform.shared.infrastructure.mongodb.converter.AccountIdWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.List;

/**
 * Global MongoDB configuration shared across all bounded contexts.
 *
 * Responsibilities:
 * - Removes the _class discriminator field from MongoDB documents.
 * - Registers custom converters for value objects that require explicit
 *   serialization rules.
 */
@Configuration
public class MongoConfig {

    /**
     * Creates a customized MongoDB mapping converter.
     *
     * The converter removes the _class field and applies the custom conversions
     * registered in mongoCustomConversions().
     *
     * @param factory MongoDB database factory
     * @param mappingContext MongoDB mapping context
     * @return customized MappingMongoConverter
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoMappingContext mappingContext
    ) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);

        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(mongoCustomConversions());

        return converter;
    }

    /**
     * Registers custom converters for value object serialization.
     *
     * InventoryState converters were removed because Inventory is no longer
     * part of the current persistence model.
     *
     * @return Mongo custom conversions
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new EmailWriteConverter(),
                new EmailReadConverter(),
                new AccountIdWriteConverter(),
                new AccountIdReadConverter(),
                new AddressWriteConverter(),
                new AddressReadConverter(),
                new ImageURLWriteConverter(),
                new ImageURLReadConverter()
        ));
    }
}