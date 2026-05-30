package com.uitopic.restock.platform.iam.domain.model.aggregates;

import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_validFields_buildsUserCorrectly() {
        Role role = new Role(RoleType.ADMIN);
        User user = new User("test@example.com", "hashed_password", role, null);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashed_password", user.getPasswordHash());
        assertEquals(role, user.getRole());
        assertNull(user.getAccountId());
    }

    @Test
    void update_newPasswordHash_changesOnlyPasswordHash() {
        Role role = new Role(RoleType.ADMIN);
        User user = new User("test@example.com", "old_hash", role, null);

        user.update("new_hash");

        assertEquals("new_hash", user.getPasswordHash());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(role, user.getRole());
    }

    @Test
    void update_newEmail_changesOnlyEmail() {
        Role role = new Role(RoleType.ADMIN);
        User user = new User("test1@example.com", "hash", role, null);

        user.update("test2@example.com");

        assertEquals("test2@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        assertEquals(role, user.getRole());
    }

    @Test
    void update_newRole_changesOnlyRole() {
        Role role1 = new Role(RoleType.ADMIN);
        Role role2 = new Role(RoleType.CASHIER);
        User user = new User("test@example.com", "hash", role1, null);

        user.update(role2);

        assertEquals(role2, user.getRole());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
    }
}
