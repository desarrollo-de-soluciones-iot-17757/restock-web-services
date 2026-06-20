package com.uitopic.restock.platform.profiles.application.internal.queryservices;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllBusinessesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByAccountIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByUserIdQuery;
import com.uitopic.restock.platform.profiles.domain.repositories.BusinessRepository;
import com.uitopic.restock.platform.profiles.domain.services.BusinessQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BusinessQueryServiceImpl implements BusinessQueryService {

    private final BusinessRepository businessRepository;

    public BusinessQueryServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> handle(GetAllBusinessesQuery query) {
        return businessRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> handle(GetBusinessByIdQuery query) {
        return businessRepository.findById(query.id());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> handle(GetBusinessByUserIdQuery query) {
        return businessRepository.findByUserId(query.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> handle(GetBusinessByAccountIdQuery query) {
        return businessRepository.findByAccountId(query.accountId().getAccountId());
    }
}
