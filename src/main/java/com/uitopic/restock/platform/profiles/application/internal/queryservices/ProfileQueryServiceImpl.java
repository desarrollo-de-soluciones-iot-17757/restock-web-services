package com.uitopic.restock.platform.profiles.application.internal.queryservices;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByAccountIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.uitopic.restock.platform.profiles.domain.repositories.ProfileRepository;
import com.uitopic.restock.platform.profiles.domain.services.ProfileQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query) {
        return profileRepository.findAll();
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.id());
    }

    @Override
    public List<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }

    @Override
    public List<Profile> handle(GetProfileByAccountIdQuery query) {
        return profileRepository.findByAccountId(query.accountId().getAccountId());
    }
}
