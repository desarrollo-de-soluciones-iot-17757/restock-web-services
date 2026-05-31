package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryTransfer;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryTransfersByBatchId;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryTransferRepository;
import com.uitopic.restock.platform.resources.domain.services.InventoryTransferQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling inventory transfer queries. This class is responsible for processing queries related to inventory transfers, such as retrieving inventory transfers by batch ID. It interacts with the InventoryTransferRepository to fetch the necessary data and provides it to the calling components. The service is marked as read-only to ensure that it does not modify any data during query operations.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class InventoryTransferQueryServiceImpl implements InventoryTransferQueryService {

    // Using the specific implementation of the repository to access custom query methods
    private final InventoryTransferRepository repository;

    // Constructor injection of the InventoryTransferRepository implementation
    public InventoryTransferQueryServiceImpl(InventoryTransferRepository repository) {
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
