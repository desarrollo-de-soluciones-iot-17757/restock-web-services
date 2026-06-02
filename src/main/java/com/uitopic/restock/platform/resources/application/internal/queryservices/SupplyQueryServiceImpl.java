package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Application service for Supply read operations.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SupplyQueryServiceImpl implements SupplyQueryService {

    private final SupplyRepository supplyRepository;

    public SupplyQueryServiceImpl(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Override
    public List<Supply> handle(GetAllSuppliesQuery query) {
        log.debug("Querying all supplies");

        var results = supplyRepository.findAll();

        log.debug("Found {} supplies", results.size());

        return results;
    }

    @Override
    public Optional<Supply> handle(GetSupplyByIdQuery query) {
        log.debug("Querying supply by id='{}'", query.supplyId());

        return supplyRepository.findById(query.supplyId());
    }

    @Override
    public List<String> handle(GetAllSupplyCategoriesQuery query) {
        log.debug("Querying all supply categories");

        var categories = supplyRepository.findAll()
                .stream()
                .map(Supply::getCategory)
                .filter(category -> category != null && !category.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();

        log.debug("Found {} supply categories", categories.size());

        return categories;
    }
}