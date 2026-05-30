package com.uitopic.restock.platform.shared.infrastructure.mongodb.configuration;

import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.InventoryStateWriteConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.StockReadConverter;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.StockWriteConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

/**
 * Configuration class for MongoDB that registers custom converters to handle the conversion of the Stock value object to and from its primitive representation in the database.
 * This allows MongoDB to store the value objects as primitive values and retrieve it correctly when reading from the database.
 */
@Configuration
public class MongoConfig {

    /**
     * Registers custom converters for MongoDB to handle the conversion of the Stock value object.
     * @return a MongoCustomConversions object containing the custom converters for Stock.
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new StockReadConverter(),
                new StockWriteConverter(),
                new InventoryStateReadConverter(),
                new InventoryStateWriteConverter()
        ));
    }
}


