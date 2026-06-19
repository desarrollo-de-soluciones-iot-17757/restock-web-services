package com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.sales.infrastructure.persistence.mongodb.entities.SalesOrderPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for SalesOrder documents.
 * Provides default CRUD operations and custom derived query methods
 * for the sales_orders collection.
 */
@Repository
public interface SalesOrderPersistenceRepository extends MongoRepository<SalesOrderPersistenceEntity, String> {

    /**
     * Finds all sales orders that belong to a specific account.
     *
     * @param accountId account identifier
     * @return a list of sales orders owned by the account
     */
    List<SalesOrderPersistenceEntity> findByAccountId(AccountId accountId);

    /**
     * Finds all sales orders processed in a specific branch.
     *
     * @param branchId branch identifier
     * @return a list of sales orders processed in the branch
     */
    List<SalesOrderPersistenceEntity> findByBranchId(String branchId);

    /**
     * Finds all sales orders with a specific operational status within a branch.
     * Useful for tracking active, completed, or cancelled orders.
     *
     * @param branchId branch identifier
     * @param status   sales order status string or enum representation
     * @return a list of sales orders matching the branch and status
     */
    List<SalesOrderPersistenceEntity> findByBranchIdAndStatus(String branchId, String status);
}