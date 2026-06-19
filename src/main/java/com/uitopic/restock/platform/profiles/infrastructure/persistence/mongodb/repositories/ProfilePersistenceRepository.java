package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.ProfilePersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilePersistenceRepository extends MongoRepository<ProfilePersistenceEntity, String> {
    List<ProfilePersistenceEntity> findByUserId(String userId);
}
