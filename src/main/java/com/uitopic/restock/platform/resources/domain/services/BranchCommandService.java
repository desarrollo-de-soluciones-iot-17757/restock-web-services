package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchImageCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;

import java.util.Optional;

/**
 * Service interface for handling commands related to Branch entities. This interface defines the contract for creating, updating, and deleting Branch entities in the system.
 * It includes methods for handling the creation of a new branch, updating existing branch information, updating the branch image, and deleting a branch by its ID.
 */
public interface BranchCommandService {

    /** Handles the creation of a new branch based on the provided CreateBranchCommand. This method validates the command, checks for any business rules (such as unique branch names within an account), and saves the new Branch entity to the repository.
     *
     * @param command the CreateBranchCommand containing the necessary information to create a new branch
     * @return the created Branch entity
     */
    Branch handle(CreateBranchCommand command);

    /** Handles the update of existing branch information based on the provided UpdateBranchInfoCommand. This method retrieves the existing Branch entity, applies the updates specified in the command, validates any business rules (such as unique branch names and locations), and saves the updated Branch entity to the repository.
     *
     * @param command the UpdateBranchInfoCommand containing the necessary information to update an existing branch
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
    Optional<Branch> handle(UpdateBranchInfoCommand command);

    /** Handles the update of a branch's image based on the provided UpdateBranchImageCommand. This method retrieves the existing Branch entity, uploads the new image using the ImageService, updates the branch's image URL and public ID, and saves the updated Branch entity to the repository.
     *
     * @param command the UpdateBranchImageCommand containing the necessary information to update a branch's image
     * @return an Optional containing the updated Branch entity if the update was successful, or empty if no Branch with the given ID exists
     */
    Optional<Branch> updateImage(UpdateBranchImageCommand command);

    /** Handles the deletion of a branch based on the provided branch ID. This method checks if a Branch with the given ID exists, and if so, deletes it from the repository.
     *
     * @param branchId the unique identifier of the branch to delete
     */
    void delete(String branchId);
}
