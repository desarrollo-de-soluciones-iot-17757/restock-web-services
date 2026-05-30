package com.uitopic.restock.platform.iam.domain.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);

    Optional<User> findById(String id);
}
