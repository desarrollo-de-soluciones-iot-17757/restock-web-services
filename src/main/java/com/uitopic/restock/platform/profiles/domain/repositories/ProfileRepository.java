package com.uitopic.restock.platform.profiles.domain.repositories;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    List<Profile> findAll();
    Optional<Profile> findById(String id);
    List<Profile> findByUserId(String userId);
    Profile save(Profile profile);
    void deleteById(String id);
}
