package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.acl.ExternalProfilesService;
import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.model.entities.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @Mock
    private ExternalProfilesService externalProfilesService;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    void handle_signUp_newEmail_savesAndReturnsUser() {
        SignUpCommand command = new SignUpCommand("My Business", "new@example.com", "password", "RESTAURANTADMIN");

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(hashingService.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(externalProfilesService.createProfileForNewUser(any(), any(), any(), any())).thenReturn("profile_id");

        User result = userCommandService.handle(command);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail().email());
        assertEquals("encoded_password", result.getPasswordHash());
        assertEquals("RESTAURANTADMIN", result.getRole().getType().name());
        verify(userRepository).save(any(User.class));
        verify(externalProfilesService).createProfileForNewUser(any(), any(), eq("My Business"), eq("new@example.com"));
    }

    @Test
    void handle_signUp_duplicateEmail_throws409Conflict() {
        SignUpCommand command = new SignUpCommand("My Business", "duplicate@example.com", "password", "RESTAURANTADMIN");

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userCommandService.handle(command));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Email already registered"));
        verify(userRepository, never()).save(any(User.class));
        verify(externalProfilesService, never()).createProfileForNewUser(any(), any(), any(), any());
    }

    @Test
    void handle_signUp_unrecognizedRole_throws400BadRequest() {
        SignUpCommand command = new SignUpCommand("My Business", "valid@example.com", "password", "UNKNOWN_ROLE");

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userCommandService.handle(command));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Unknown role"));
        verify(userRepository, never()).save(any(User.class));
        verify(externalProfilesService, never()).createProfileForNewUser(any(), any(), any(), any());
    }

    @Test
    void handle_signIn_validCredentials_returnsTokenData() {
        SignInCommand command = new SignInCommand("user@example.com", "correct_password");
        User user = new User(new Email("user@example.com"), "hashed_password", new Role(RoleType.RESTAURANTADMIN), null);

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(hashingService.matches("correct_password", "hashed_password")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("jwt.token.here");

        var result = userCommandService.handle(command);

        assertTrue(result.isPresent());
        assertEquals(user, result.get().getKey());
        assertEquals("jwt.token.here", result.get().getValue());
        assertEquals("user@example.com", result.get().getKey().getEmail().email());
        assertEquals("RESTAURANTADMIN", result.get().getKey().getRole().getType().name());
    }

    @Test
    void handle_signIn_wrongPassword_returnsEmpty() {
        SignInCommand command = new SignInCommand("user@example.com", "wrong_password");
        User user = new User(new Email("user@example.com"), "hashed_password", new Role(RoleType.RESTAURANTADMIN), null);

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));
        when(hashingService.matches("wrong_password", "hashed_password")).thenReturn(false);

        var result = userCommandService.handle(command);

        assertFalse(result.isPresent());
    }

    @Test
    void handle_signIn_unknownEmail_returnsEmpty() {
        SignInCommand command = new SignInCommand("unknown@example.com", "password");

        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        var result = userCommandService.handle(command);

        assertFalse(result.isPresent());
    }
}
