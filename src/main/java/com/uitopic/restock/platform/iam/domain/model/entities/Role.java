package com.uitopic.restock.platform.iam.domain.model.entities;

import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a Role entity that defines the authorization level of a user.
 * Each role contains a specific role type which dictates privileges.
 */
@Getter
@NoArgsConstructor
public class Role {

    private String id;

    private RoleType type;

    /**
     * Constructs a Role with the specified RoleType.
     *
     * @param type the type of the role to assign
     */
    public Role(RoleType type) {
        this.type = type;
    }

    /**
     * Gets the type of the role.
     *
     * @return the RoleType of this role
     */
    public RoleType getType() {
        return type;
    }
}
