package com.uitopic.restock.platform.profiles.domain.services;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetAllProfilesQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.uitopic.restock.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    List<Profile> handle(GetAllProfilesQuery query);
    Optional<Profile> handle(GetProfileByIdQuery query);
    List<Profile> handle(GetProfileByUserIdQuery query);
}
