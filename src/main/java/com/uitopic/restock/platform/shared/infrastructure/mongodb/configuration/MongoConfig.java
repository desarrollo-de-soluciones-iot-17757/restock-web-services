package com.uitopic.restock.platform.shared.infrastructure.mongodb.configuration;

import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailReadConverter;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.converters.EmailWriteConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.AddressReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.AddressWriteConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.ImageURLReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.ImageURLWriteConverter;
//import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateReadConverter;
//import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateWriteConverter;
import com.uitopic.restock.platform.shared.infrastructure.mongodb.converter.*;
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
 * <p>Responsibilities:
 * <ul>
 *   <li>Suppresses the {@code _class} discriminator field from all documents
 *       by setting a {@code null} type key on the {@link MappingMongoConverter}.</li>
 *   <li>Registers all custom converters so value objects are stored as
 *       primitives instead of embedded documents:
 *     <ul>
 *       <li>IAM: {@code Email} ↔ {@code String}</li>
 *       <li>Resources: {@code Stock} ↔ {@code Double}</li>
 *       <li>Resources: {@code InventoryState} ↔ {@code String}</li>
 *     </ul>
 *   </li>
 * </ul>
 */
@Configuration
public class MongoConfig {
    /**
     * Overrides the default {@link MappingMongoConverter} to:
     * <ol>
     *   <li>Remove the {@code _class} field from every document.</li>
     *   <li>Apply all custom converters for value object serialization.</li>
     * </ol>
     *
     * @param factory        the MongoDB database factory
     * @param mappingContext the mapping context used to resolve entity metadata
     * @return a customized {@link MappingMongoConverter}
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory,
                                                       MongoMappingContext mappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(mongoCustomConversions());

        return converter;
    }

    /**
     * Registers all custom converters for value object serialization. This ensures that value objects are stored as primitives in MongoDB, rather than as embedded documents.
     *
     * @return a {@link MongoCustomConversions} instance containing all registered converters
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new InventoryStateReadConverter(),
                new InventoryStateWriteConverter()

                new EmailWriteConverter(),
                new EmailReadConverter(),
                new AccountIdReadConverter(),
<<<<<<< HEAD
                new AccountIdWriteConverter()
=======
                new AccountIdWriteConverter(),
                new AddressWriteConverter(),
                new AddressReadConverter(),
                new ImageURLWriteConverter(),
                new ImageURLReadConverter()
                //new StockWriteConverter(),
                //new StockReadConverter(),
                //new InventoryStateWriteConverter(),
                //new InventoryStateReadConverter()
>>>>>>> origin/develop
        ));
    }
}