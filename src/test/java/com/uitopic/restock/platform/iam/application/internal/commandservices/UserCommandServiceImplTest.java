package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
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

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @Test
    void handle_signUp_newEmail_savesAndReturnsUser() {
        SignUpCommand command = new SignUpCommand("My Business", "new@example.com", "password", "CASHIER");
        
        when(userRepository.existsByEmailValue("new@example.com")).thenReturn(false);
        when(hashingService.encode("password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userCommandService.handle(command);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail().email());
        assertEquals("encoded_password", result.getPasswordHash());
        assertEquals("CASHIER", result.getRole().getType().name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void handle_signUp_duplicateEmail_throws409Conflict() {
        SignUpCommand command = new SignUpCommand("My Business", "duplicate@example.com", "password", "CASHIER");
        
        when(userRepository.existsByEmailValue("duplicate@example.com")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userCommandService.handle(command);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Email already registered"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void handle_signUp_invalidEmailFormat_throws400BadRequest() {
        SignUpCommand command = new SignUpCommand("My Business", "invalid-email", "password", "CASHIER");
        
        when(userRepository.existsByEmailValue("invalid-email")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userCommandService.handle(command);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void handle_signUp_unrecognizedRole_throws400BadRequest() {
        SignUpCommand command = new SignUpCommand("My Business", "valid@example.com", "password", "UNKNOWN_ROLE");
        
        when(userRepository.existsByEmailValue("valid@example.com")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userCommandService.handle(command);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Unknown role"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void handle_signIn_validCredentials_returnsUser() {
        SignInCommand command = new SignInCommand("user@example.com", "correct_password");
        User user = new User(new com.uitopic.restock.platform.iam.domain.model.valueobjects.Email("user@example.com"), "hashed_password", new com.uitopic.restock.platform.iam.domain.model.entities.Role(com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType.ADMIN), null);

        when(userRepository.findByEmailValue("user@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("correct_password", "hashed_password")).thenReturn(true);

        Optional<User> result = userCommandService.handle(command);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void handle_signIn_wrongPassword_returnsEmpty() {
        SignInCommand command = new SignInCommand("user@example.com", "wrong_password");
        User user = new User(new com.uitopic.restock.platform.iam.domain.model.valueobjects.Email("user@example.com"), "hashed_password", new com.uitopic.restock.platform.iam.domain.model.entities.Role(com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType.ADMIN), null);

        when(userRepository.findByEmailValue("user@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("wrong_password", "hashed_password")).thenReturn(false);

        Optional<User> result = userCommandService.handle(command);

        assertFalse(result.isPresent());
    }

    @Test
    void handle_signIn_unknownEmail_returnsEmpty() {
        SignInCommand command = new SignInCommand("unknown@example.com", "password");

        when(userRepository.findByEmailValue("unknown@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userCommandService.handle(command);

        assertFalse(result.isPresent());
    }
}
