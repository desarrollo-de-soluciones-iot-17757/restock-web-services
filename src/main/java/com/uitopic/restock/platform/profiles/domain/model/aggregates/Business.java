package com.uitopic.restock.platform.profiles.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Business extends AbstractDomainAggregateRoot<Business> {

    private String id;
    private String accountId;
    private String userId;
    private String ruc;
    private String pictureUrl;
    private String picturePublicId;
    private String companyName;
    private String mainLocation;

    @Builder
    public Business(String accountId, String userId, String ruc, String pictureUrl, String picturePublicId,
                    String companyName, String mainLocation) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("User ID cannot be blank");
        this.accountId = accountId;
        this.userId = userId;
        this.ruc = ruc;
        this.pictureUrl = pictureUrl;
        this.picturePublicId = picturePublicId;
        this.companyName = companyName;
        this.mainLocation = mainLocation;
    }

    public Business update(String ruc, String pictureUrl, String picturePublicId,
                           String companyName, String mainLocation) {
        if (ruc != null) this.ruc = ruc;
        if (pictureUrl != null) this.pictureUrl = pictureUrl;
        if (picturePublicId != null) this.picturePublicId = picturePublicId;
        if (companyName != null && !companyName.isBlank()) this.companyName = companyName;
        if (mainLocation != null) this.mainLocation = mainLocation;
        return this;
    }
}
