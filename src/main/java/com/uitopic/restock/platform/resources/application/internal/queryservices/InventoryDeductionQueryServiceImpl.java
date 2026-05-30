package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryDeductionsByBatchId;
import com.uitopic.restock.platform.resources.domain.services.InventoryDeductionQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.InventoryDeductionRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class InventoryDeductionQueryServiceImpl implements InventoryDeductionQueryService {

    private final InventoryDeductionRepositoryImpl repository;

    public InventoryDeductionQueryServiceImpl(InventoryDeductionRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<InventoryDeduction> handle(GetInventoryDeductionsByBatchId query) {
        log.debug("Querying inventory deductions for branchId={}", query.branchId());
        return repository.findByBranchId(query.branchId());
    }

    @Override
    public Optional<InventoryDeduction> findById(String id) {
        return repository.findById(id);
    }
}
