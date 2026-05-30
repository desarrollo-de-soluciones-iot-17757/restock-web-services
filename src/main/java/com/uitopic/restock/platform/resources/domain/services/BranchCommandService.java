package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.commands.CreateBranchCommand;
import com.uitopic.restock.platform.resources.domain.model.commands.UpdateBranchInfoCommand;

import java.util.Optional;

public interface BranchCommandService {
    Branch handle(CreateBranchCommand command);
    Optional<Branch> handle(UpdateBranchInfoCommand command);
    Optional<Branch> updateImage(String branchId, String imageUrl);
    void delete(String branchId);
}
