package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Email;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.Role;
import com.uitopic.restock.platform.iam.domain.model.valueobjects.RoleType;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    @Override
    public Optional<User> handle(SignInCommand command) {
        return userRepository.findByEmailValue(command.email())
                .filter(user -> hashingService.matches(command.password(), user.getPasswordHash()));
    }

    @Override
    public User handle(SignUpCommand command) {
        if (userRepository.existsByEmailValue(command.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered: " + command.email());

        Email email;
        try {
            email = new Email(command.email());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        RoleType roleType;
        try {
            roleType = RoleType.valueOf(command.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown role: " + command.role());
        }

        String passwordHash = hashingService.encode(command.password());

        // businessName will be used for Account creation once that bounded context is integrated
        User user = new User(email, passwordHash, new Role(roleType), null);
        return userRepository.save(user);
    }
}
