package com.uitopic.restock.platform.iam.domain.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;

import java.util.Optional;

public interface UserCommandService {

    Optional<User> handle(SignInCommand command);

    User handle(SignUpCommand command);
}
