package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.model.entities.SalesOrderItem;
import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities.SalesOrderPersistenceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class SalesOrderPersistenceAssembler {

    private SalesOrderPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static SalesOrder toDomainFromPersistence(SalesOrderPersistenceEntity entity) {
        if (entity == null) return null;

        var salesOrder = new SalesOrder();
        salesOrder.setId(entity.getId());
        salesOrder.setAccountId(entity.getAccountId());
        salesOrder.setBranchId(entity.getBranchId());

        salesOrder.setStatus(entity.getStatus());
        salesOrder.setPricing(entity.getPricing());
        salesOrder.setCreatedAt(entity.getRegisteredAt());

        if (entity.getItems() != null) {
            List<SalesOrderItem> domainItems = entity.getItems().stream().map(item -> {
                if (item.getId() == null) {
                    item.setId(UUID.randomUUID().toString());
                }
                return item;
            }).collect(Collectors.toList());

            salesOrder.setItems(domainItems);
        }
        return salesOrder;
    }

    public static SalesOrderPersistenceEntity toPersistenceFromDomain(SalesOrder domain) {
        if (domain == null) return null;

        var entity = new SalesOrderPersistenceEntity();
        if (domain.getId() != null) entity.setId(domain.getId());

        entity.setAccountId(domain.getAccountId());
        entity.setBranchId(domain.getBranchId());

        entity.setStatus(domain.getStatus());
        entity.setPricing(domain.getPricing());
        entity.setRegisteredAt(domain.getCreatedAt());

        if (domain.getItems() != null) {
            entity.setItems(new ArrayList<>(domain.getItems()));
        }

        return entity;
    }
}