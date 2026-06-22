package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.BusinessPersistenceEntity;

public final class BusinessPersistenceAssembler {

    private BusinessPersistenceAssembler() {}

    public static Business toDomainFromPersistence(BusinessPersistenceEntity entity) {
        if (entity == null) return null;
        var business = new Business();
        business.setId(entity.getId());
        business.setAccountId(entity.getAccountId());
        business.setUserId(entity.getUserId());
        business.setRuc(entity.getRuc());
        business.setPictureUrl(entity.getPictureUrl());
        business.setPicturePublicId(entity.getPicturePublicId());
        business.setCompanyName(entity.getCompanyName());
        business.setMainLocation(entity.getMainLocation());
        return business;
    }

    public static BusinessPersistenceEntity toPersistenceFromDomain(Business business) {
        if (business == null) return null;
        var entity = new BusinessPersistenceEntity();
        if (business.getId() != null) entity.setId(business.getId());
        entity.setAccountId(business.getAccountId());
        entity.setUserId(business.getUserId());
        entity.setRuc(business.getRuc());
        entity.setPictureUrl(business.getPictureUrl());
        entity.setPicturePublicId(business.getPicturePublicId());
        entity.setCompanyName(business.getCompanyName());
        entity.setMainLocation(business.getMainLocation());
        return entity;
    }
}
