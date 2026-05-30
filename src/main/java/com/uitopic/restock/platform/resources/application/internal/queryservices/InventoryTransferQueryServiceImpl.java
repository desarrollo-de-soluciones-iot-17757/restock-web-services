package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryTransfersByBatchId;
import com.uitopic.restock.platform.resources.domain.services.InventoryTransferQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.InventoryTransferRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class InventoryTransferQueryServiceImpl implements InventoryTransferQueryService {

    private final InventoryTransferRepositoryImpl repository;

    public InventoryTransferQueryServiceImpl(InventoryTransferRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<InventoryTransfer> handle(GetInventoryTransfersByBatchId query) {
        log.debug("Querying inventory transfers for branchId={}", query.branchId());
        return repository.findByFromBranchId(query.branchId());
    }

    @Override
    public Optional<InventoryTransfer> findById(String id) {
        return repository.findById(id);
    }
}
