package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllCustomSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for CustomSupply read operations.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomSupplyQueryServiceImpl implements CustomSupplyQueryService {

    private final CustomSupplyRepository customSupplyRepository;

    public CustomSupplyQueryServiceImpl(CustomSupplyRepository customSupplyRepository) {
        this.customSupplyRepository = customSupplyRepository;
    }

    @Override
    public List<CustomSupply> handle(GetAllCustomSuppliesQuery query) {
        log.debug("Querying all custom supplies");

        var results = customSupplyRepository.findAll();

        log.debug("Found {} custom supplies", results.size());

        return results;
    }

    @Override
    public List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query) {
        log.debug("Querying custom supplies by accountId='{}'", query.accountId());

        var results = customSupplyRepository.findByAccountId(query.accountId());

        log.debug("Found {} custom supplies for accountId='{}'", results.size(), query.accountId());

        return results;
    }

    @Override
    public Optional<CustomSupply> handle(GetCustomSupplyByIdQuery query) {
        log.debug("Querying custom supply by id='{}'", query.customSupplyId());

        return customSupplyRepository.findById(query.customSupplyId());
    }
}