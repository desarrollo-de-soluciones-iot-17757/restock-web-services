package com.uitopic.restock.platform.profiles.domain.services;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllBusinessesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetBusinessByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface BusinessQueryService {
    List<Business> handle(GetAllBusinessesQuery query);
    Optional<Business> handle(GetBusinessByIdQuery query);
    List<Business> handle(GetBusinessByUserIdQuery query);
}
