package com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Configuration class that initializes MongoDB collections on application startup. It checks for the existence of specified collections and creates them if they do not exist.
 */
@Configuration
public class CollectionInitializer {

    // List of collection names to be created if they do not exist
    private static final List<String> COLLECTION_NAMES = List.of(
            "users",
            "roles",
            "recipes",
            "batches",
            "custom_supplies",
            "devices",
            "orders",
            "supplies",
            "kits",
            "sales",
            "notifications",
            "branches",
            "discrepancies",
            "telemetry_readings",
            "stock_comparison_tasks",
            "push_subscriptions",
            "device_thresholds",
            "accounts",
            "plans",
            "subscriptions"
    );

    /**
     * ApplicationRunner bean that checks for the existence of specified collections and creates them if they do not exist.
     * @param mongoTemplate the MongoTemplate used to interact with the MongoDB database
     * @return an ApplicationRunner that initializes the collections on application startup
     */
    @Bean
    public ApplicationRunner collectionCreator(MongoTemplate mongoTemplate) {
        return args -> COLLECTION_NAMES.stream()
                .filter(collectionName -> !mongoTemplate.collectionExists(collectionName))
                .forEach(mongoTemplate::createCollection);
    }
}
