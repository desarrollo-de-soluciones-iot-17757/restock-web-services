package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.DeleteBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchStatusCommand;

import java.util.Optional;

/**
 * Command service contract for Branch write operations.
 */
public interface BranchCommandService {

    /**
     * Creates a new branch.
     *
     * @param command command with the branch data
     * @return created branch
     */
    Branch handle(CreateBranchCommand command);

    /**
     * Updates an existing branch.
     *
     * @param command command with the updated branch data
     * @return updated branch, or empty if not found
     */
    Optional<Branch> handle(UpdateBranchCommand command);

    /**
     * Deactivates an existing branch.
     *
     * @param command command with the branch identifier
     */
    void handle(DeleteBranchCommand command);

    /**
     * Updates status of an existing branch.
     *
     * @param command command with the updated status branch
     * @return updated branch, or empty if not found
     */
    Optional<Branch> handle(UpdateBranchStatusCommand command);

}