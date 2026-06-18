package com.uitopic.restock.platform.profiles.infrastructure.adapters;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.domain.repositories.ProfileRepository;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers.ProfilePersistenceAssembler;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.repositories.ProfilePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfilePersistenceRepository profilePersistenceRepository;

    public ProfileRepositoryImpl(ProfilePersistenceRepository profilePersistenceRepository) {
        this.profilePersistenceRepository = profilePersistenceRepository;
    }

    @Override
    public List<Profile> findAll() {
        return profilePersistenceRepository.findAll().stream()
                .map(ProfilePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Profile> findById(String id) {
        return profilePersistenceRepository.findById(id)
                .map(ProfilePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Profile> findByUserId(String userId) {
        return profilePersistenceRepository.findByUserId(userId).stream()
                .map(ProfilePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Profile save(Profile profile) {
        var saved = profilePersistenceRepository.save(ProfilePersistenceAssembler.toPersistenceFromDomain(profile));
        return ProfilePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(String id) {
        profilePersistenceRepository.deleteById(id);
    }
}
