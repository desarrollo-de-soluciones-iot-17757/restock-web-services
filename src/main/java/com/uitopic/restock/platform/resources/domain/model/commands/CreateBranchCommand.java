package com.uitopic.restock.platform.resources.domain.model.commands;

public record CreateBranchCommand(
        String accountId,
        String name,
        String address,
        String city,
        String country,
        String imageUrl,
        String description
) {}
