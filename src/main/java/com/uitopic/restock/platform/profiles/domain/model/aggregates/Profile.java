package com.uitopic.restock.platform.profiles.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Profile extends AbstractDomainAggregateRoot<Profile> {

    private static final String DEFAULT_AVATAR_URL =
            "https://res.cloudinary.com/deuy1pr9e/image/upload/v1759710739/Default-profile_xbpv55.jpg";

    private static final String DEFAULT_AVATAR_PUBLIC_ID =
            "Default-profile_xbpv55";

    private String id;
    private String accountId;
    private String userId;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private String avatarPublicId;
    private String gender;
    private String birthDate;

    @Builder
    public Profile(String accountId, String userId, String name, String lastName, String phoneNumber,
                   String avatarUrl, String avatarPublicId, String gender, String birthDate) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("User ID cannot be blank");
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        applyAvatar(avatarUrl, avatarPublicId);
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
        applyAvatar(this.avatarUrl, this.avatarPublicId);
        return this;
    }

    public void normalizeAvatar() {
        applyAvatar(this.avatarUrl, this.avatarPublicId);
    }

    public boolean hasDefaultAvatar() {
        return DEFAULT_AVATAR_PUBLIC_ID.equals(this.avatarPublicId);
    }

    private void applyAvatar(String avatarUrl, String avatarPublicId) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            this.avatarUrl = DEFAULT_AVATAR_URL;
            this.avatarPublicId = DEFAULT_AVATAR_PUBLIC_ID;
            return;
        }

        this.avatarUrl = avatarUrl;
        this.avatarPublicId = avatarPublicId;
    }
}
