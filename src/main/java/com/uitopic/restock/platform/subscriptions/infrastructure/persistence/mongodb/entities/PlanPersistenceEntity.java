package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.PlanPrice;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanLimits;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "plans")
public class PlanPersistenceEntity {
    @Id
    private String id;
    private String name;
    private String description;
    @Indexed(unique = true)
    private PlanType type;
    private String stripeProductId;
    private PlanLimits limits;
    private List<String> benefits = new ArrayList<>();
    private List<PlanPrice> prices = new ArrayList<>();
    private boolean active;
}
