package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "plans")
public class PlanPersistenceEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String billingInterval;
    private String stripePriceId;
    private int maxRecipes;
    private int maxKits;
    private int maxDevices;
}
