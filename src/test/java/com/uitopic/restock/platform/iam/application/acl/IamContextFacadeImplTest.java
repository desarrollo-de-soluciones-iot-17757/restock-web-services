package com.uitopic.restock.platform.iam.application.acl;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IamContextFacadeImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IamContextFacadeImpl iamContextFacade;

    @Test
    void existsUserByEmail_registeredEmail_returnsTrue() {
        String email = "registered@example.com";
        when(userRepository.existsByEmailValue(email)).thenReturn(true);

        boolean result = iamContextFacade.existsUserByEmail(email);

        assertTrue(result);
        verify(userRepository).existsByEmailValue(email);
    }

    @Test
    void existsUserByEmail_unknownEmail_returnsFalse() {
        String email = "unknown@example.com";
        when(userRepository.existsByEmailValue(email)).thenReturn(false);

        boolean result = iamContextFacade.existsUserByEmail(email);

        assertFalse(result);
        verify(userRepository).existsByEmailValue(email);
    }

    @Test
    void fetchAccountIdByUserId_existingUserWithAccount_returnsAccountId() {
        String userId = "user123";
        AccountId accountId = new AccountId("acc456");
        User user = new User(new Email("test@example.com"), "hashed_password", new Role(RoleType.ADMIN), accountId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = iamContextFacade.fetchAccountIdByUserId(userId);

        assertEquals("acc456", result);
        verify(userRepository).findById(userId);
    }

    @Test
    void fetchAccountIdByUserId_existingUserWithoutAccount_returnsEmptyString() {
        String userId = "user123";
        User user = new User(new Email("test@example.com"), "hashed_password", new Role(RoleType.ADMIN), null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = iamContextFacade.fetchAccountIdByUserId(userId);

        assertEquals("", result);
        verify(userRepository).findById(userId);
    }

    @Test
    void fetchAccountIdByUserId_unknownUserId_returnsEmptyString() {
        String userId = "unknown_user";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String result = iamContextFacade.fetchAccountIdByUserId(userId);

        assertEquals("", result);
        verify(userRepository).findById(userId);
    }
}
