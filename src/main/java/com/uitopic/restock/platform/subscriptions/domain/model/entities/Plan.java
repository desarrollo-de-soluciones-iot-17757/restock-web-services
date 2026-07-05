package com.uitopic.restock.platform.subscriptions.domain.model.entities;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@EqualsAndHashCode
public class Plan {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String billingInterval; // e.g., "month", "year"
    private String stripePriceId;
    private int maxRecipes;
    private int maxKits;
    private int maxDevices;
}
