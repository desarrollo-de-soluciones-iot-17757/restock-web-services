package com.uitopic.restock.platform.tracking.application.internal.queryservices;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepanciesQuery;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetDiscrepancyByIdQuery;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.domain.services.DiscrepancyQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application query service for discrepancy read operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscrepancyQueryServiceImpl implements DiscrepancyQueryService {
    private final DiscrepancyRepository discrepancyRepository;

    @Override
    public List<Discrepancy> handle(GetDiscrepanciesQuery query) {
        log.debug("Querying discrepancies with status='{}'", query.status());
        return discrepancyRepository.findAllByFilters(query.status());
    }

    @Override
    public Optional<Discrepancy> handle(GetDiscrepancyByIdQuery query) {
        log.debug("Querying discrepancy by id='{}'", query.discrepancyId());
        return discrepancyRepository.findById(query.discrepancyId());
    }
}
