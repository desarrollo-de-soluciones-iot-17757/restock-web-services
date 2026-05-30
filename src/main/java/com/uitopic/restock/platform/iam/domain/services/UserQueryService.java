package com.uitopic.restock.platform.iam.domain.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

public interface UserQueryService {

    Optional<User> handle(GetUserByIdQuery query);
}
