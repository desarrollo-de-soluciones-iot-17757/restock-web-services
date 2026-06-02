package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand;

import java.util.Optional;

/**
 * Domain service interface defining the command contract for {@link Branch} aggregate operations
 * within the resources bounded context.
 *
 * <p>Declares the write-side operations available on branches: creation, info update,
 * image update, and logical deletion. Implementations live in the application layer
 * ({@link com.uitopic.restock.platform.resources.application.internal.commandservices.BranchCommandServiceImpl}).
 */
public interface BranchCommandService {

    /**
     * Handles the creation of a new branch for the given account.
     * Validates that the branch name is unique within the account before persisting.
     *
     * @param command the command containing all data required to create the branch
     * @return the newly created and persisted {@link Branch} aggregate
     * @throws org.springframework.web.server.ResponseStatusException with 400 if the branch name already exists
     */
    Branch handle(CreateBranchCommand command);

    /**
     * Handles a partial or full update of an existing branch's information.
     * Only non-null fields in the command are applied. Validates name uniqueness if the name changes.
     *
     * @param command the command containing the branch ID and the fields to update
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     * @throws com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist if the new name conflicts with another branch in the same account
     */
    Optional<Branch> handle(UpdateBranchInfoCommand command);

    /**
     * Updates the image of an existing branch by uploading the new image via the storage service,
     * updating the branch's image URL and public ID, and persisting the changes.
     *
     * @param command the command containing the branch ID and the new image data
     * @return an {@link Optional} containing the updated {@link Branch}, or empty if not found
     */
    Optional<Branch> handle(UpdateBranchImageCommand command);

    /**
     * Updates the status of a branch.
     * @param command the command containing the branch ID and the new status
     */
    void handle(UpdateBranchStatusCommand command);
}