package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.SupplyRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Query service implementation for {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply}
 * entities within the resources bounded context.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SupplyQueryServiceImpl implements SupplyQueryService {

    private final SupplyRepositoryImpl supplyRepository;

    public SupplyQueryServiceImpl(SupplyRepositoryImpl supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Override
    public List<Supply> handle(GetAllSuppliesQuery query) {
        log.debug("Querying all supplies templates");
        var results = supplyRepository.findAll();
        log.debug("Found {} supplies templates", results.size());
        return results;
    }

    @Override
    public Optional<Supply> findById(String id) {
        return supplyRepository.findById(id);
    }
}
