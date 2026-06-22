package com.uitopic.restock.platform.profiles.interfaces.rest.transform;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.BusinessResource;

public final class BusinessResourceFromEntityAssembler {

    private BusinessResourceFromEntityAssembler() {}

    public static BusinessResource toResourceFromEntity(Business business) {
        return new BusinessResource(
                business.getId(),
                business.getAccountId(),
                business.getUserId(),
                business.getRuc(),
                business.getPictureUrl(),
                business.getPicturePublicId(),
                business.getCompanyName(),
                business.getMainLocation()
        );
    }
}
