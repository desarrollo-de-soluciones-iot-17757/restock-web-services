package com.uitopic.restock.platform.resources.domain.model.commands;

public record UpdateBranchInfoCommand(
        String branchId,
        String name,
        String address,
        String city,
        String country,
        String description
) {}
