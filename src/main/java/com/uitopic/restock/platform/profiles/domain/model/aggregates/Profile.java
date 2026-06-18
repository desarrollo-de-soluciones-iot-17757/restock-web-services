package com.uitopic.restock.platform.profiles.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Profile extends AbstractDomainAggregateRoot<Profile> {

    private String id;
    private String userId;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private String avatarPublicId;
    private String gender;
    private String birthDate;

    @Builder
    public Profile(String userId, String name, String lastName, String phoneNumber,
                   String avatarUrl, String avatarPublicId, String gender, String birthDate) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("User ID cannot be blank");
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.avatarPublicId = avatarPublicId;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public Profile update(String name, String lastName, String phoneNumber,
                          String avatarUrl, String avatarPublicId, String gender, String birthDate) {
        if (name != null && !name.isBlank()) this.name = name;
        if (lastName != null) this.lastName = lastName;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (avatarUrl != null) this.avatarUrl = avatarUrl;
        if (avatarPublicId != null) this.avatarPublicId = avatarPublicId;
        if (gender != null) this.gender = gender;
        if (birthDate != null) this.birthDate = birthDate;
        return this;
    }
}
