package com.uitopic.restock.platform.iam.application.internal.commandservices;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.repositories.UserRepository;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import org.springframework.stereotype.Service;

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
        return userRepository.findByEmail(command.email())
                .filter(user -> hashingService.matches(command.password(), user.getPasswordHash()));
    }
}
