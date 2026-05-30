package com.uitopic.restock.platform.iam.domain.model.aggregates;

import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void handle_validFields_buildsUserCorrectly() {
        Email email = new Email("test@example.com");
        Role role = new Role(RoleType.ADMIN);
        User user = new User(email, "hashed_password", role, null);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("hashed_password", user.getPasswordHash());
        assertEquals(role, user.getRole());
        assertNull(user.getAccountId());
    }

    @Test
    void update_newPasswordHash_changesOnlyPasswordHash() {
        Email email = new Email("test@example.com");
        Role role = new Role(RoleType.ADMIN);
        User user = new User(email, "old_hash", role, null);

        user.update("new_hash");

        assertEquals("new_hash", user.getPasswordHash());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
    }

    @Test
    void update_newEmail_changesOnlyEmail() {
        Email email1 = new Email("test1@example.com");
        Email email2 = new Email("test2@example.com");
        Role role = new Role(RoleType.ADMIN);
        User user = new User(email1, "hash", role, null);

        user.update(email2);

        assertEquals(email2, user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        assertEquals(role, user.getRole());
    }

    @Test
    void update_newRole_changesOnlyRole() {
        Email email = new Email("test@example.com");
        Role role1 = new Role(RoleType.ADMIN);
        Role role2 = new Role(RoleType.CASHIER);
        User user = new User(email, "hash", role1, null);

        user.update(role2);

        assertEquals(role2, user.getRole());
        assertEquals(email, user.getEmail());
        assertEquals("hash", user.getPasswordHash());
    }

    @Test
    void emailConstructor_invalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void emailConstructor_validFormat_createsEmail() {
        Email email = new Email("valid@example.com");
        assertNotNull(email);
        assertEquals("valid@example.com", email.email());
        assertEquals("valid@example.com", email.getEmail());
    }
}
