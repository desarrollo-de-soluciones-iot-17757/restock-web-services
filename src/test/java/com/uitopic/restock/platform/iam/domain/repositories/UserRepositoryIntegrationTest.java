package com.uitopic.restock.platform.iam.domain.repositories;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void save_validUser_persistsAndRetrievableByEmail() {
        User user = new User(new Email("test@example.com"), "hashed_password", new Role(RoleType.RETAILADMIN), null);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());

        Optional<User> foundUser = userRepository.findByEmail(new Email("test@example.com"));
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail().email());
    }

    @Test
    void existsByEmail_existingEmail_returnsTrue() {
        User user = new User(new Email("existing@example.com"), "hashed_password", new Role(RoleType.RESTAURANTADMIN), null);
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail(new Email("existing@example.com"));
        assertTrue(exists);
    }

    @Test
    void existsByEmail_unknownEmail_returnsFalse() {
        boolean exists = userRepository.existsByEmail(new Email("unknown@example.com"));
        assertFalse(exists);
    }

    @Test
    void save_duplicateEmail_throwsException() {
        User user1 = new User(new Email("duplicate@example.com"), "password123", new Role(RoleType.RESTAURANTADMIN), null);
        User user2 = new User(new Email("duplicate@example.com"), "password456", new Role(RoleType.RETAILADMIN), null);

        userRepository.save(user1);

        assertThrows(DuplicateKeyException.class, () -> userRepository.save(user2));
    }
}
