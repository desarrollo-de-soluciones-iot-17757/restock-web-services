package com.uitopic.restock.platform.sales.infrastructure.adapters;

import com.uitopic.restock.platform.sales.domain.model.aggregates.SalesOrder;
import com.uitopic.restock.platform.sales.domain.repositories.SalesOrderRepository;
import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.assemblers.SalesOrderPersistenceAssembler;
import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.repositories.SalesOrderPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the SalesOrderRepository domain port.
 * Acts as a bridge between the domain repository contract and the Spring Data
 * MongoDB repository.
 */
@Repository
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final SalesOrderPersistenceRepository salesOrderPersistenceRepository;

    public SalesOrderRepositoryImpl(SalesOrderPersistenceRepository salesOrderPersistenceRepository) {
        this.salesOrderPersistenceRepository = salesOrderPersistenceRepository;
    }

    @Override
    public SalesOrder save(SalesOrder order) {
        var entity = SalesOrderPersistenceAssembler.toPersistenceFromDomain(order);
        var saved = salesOrderPersistenceRepository.save(entity);
        return SalesOrderPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<SalesOrder> findById(String id) {
        return salesOrderPersistenceRepository.findById(id)
                .map(SalesOrderPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<SalesOrder> findAll() {
        return salesOrderPersistenceRepository.findAll()
                .stream()
                .map(SalesOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<SalesOrder> findByAccountId(AccountId accountId) {
        return salesOrderPersistenceRepository.findByAccountId(accountId)
                .stream()
                .map(SalesOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<SalesOrder> findByBranchId(String branchId) {
        return salesOrderPersistenceRepository.findByBranchId(branchId)
                .stream()
                .map(SalesOrderPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        salesOrderPersistenceRepository.deleteById(id);
    }
}