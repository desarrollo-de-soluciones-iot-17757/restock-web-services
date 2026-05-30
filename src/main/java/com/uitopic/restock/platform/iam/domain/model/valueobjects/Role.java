package com.uitopic.restock.platform.iam.domain.model.valueobjects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Role {

    private String id;
    private RoleType type;

    public Role(RoleType type) {
        this.type = type;
    }
}
