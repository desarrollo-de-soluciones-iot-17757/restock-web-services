package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "branches")
public class Branch extends AuditableAbstractAggregateRoot {

    private AccountId accountId;
    private String name;
    private String address;
    private String city;
    private String country;
    private ImageURL imageUrl;
    private String status;
    private String description;

    public void update(String address, String city, String country, String description, String name) {
        if (name != null && !name.isBlank()) this.name = name;
        if (address != null) this.address = address;
        if (city != null) this.city = city;
        if (country != null) this.country = country;
        if (description != null) this.description = description;
    }

    public void deactivate() {
        this.status = "inactive";
    }

    public void activate() {
        this.status = "active";
    }
}
