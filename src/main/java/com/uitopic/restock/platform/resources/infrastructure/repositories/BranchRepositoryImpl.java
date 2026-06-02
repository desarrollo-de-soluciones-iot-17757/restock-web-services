package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BranchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the BranchRepository domain port.
 *
 * Acts as a bridge between the domain repository contract and the Spring Data
 * MongoDB repository.
 */
@Repository
public class BranchRepositoryImpl implements BranchRepository {

    /**
     * Find all branches.
     *
     * @return list of branches
     */
    @Override
    public List<Branch> findAll() {
        return branchMongoRepository.findAll();
    }

    /**
     * Spring Data MongoDB repository used to access branch documents.
     */
    private final BranchMongoRepository branchMongoRepository;

    /**
     * Creates a BranchRepositoryImpl with the required MongoDB repository.
     *
     * @param branchMongoRepository MongoDB repository for branches
     */
    public BranchRepositoryImpl(BranchMongoRepository branchMongoRepository) {
        this.branchMongoRepository = branchMongoRepository;
    }

    /**
     * Saves a branch.
     *
     * @param branch branch to save
     * @return saved branch
     */
    @Override
    public Branch save(Branch branch) {
        return branchMongoRepository.save(branch);
    }

    /**
     * Finds a branch by its identifier.
     *
     * @param id branch identifier
     * @return branch if found
     */
    @Override
    public Optional<Branch> findById(String id) {
        return branchMongoRepository.findById(id);
    }

    /**
     * Finds all branches that belong to an account.
     *
     * @param accountId account identifier
     * @return branches owned by the account
     */
    @Override
    public List<Branch> findByAccountId(AccountId accountId) {
        return branchMongoRepository.findByAccountId(accountId);
    }

    /**
     * Checks whether a branch name already exists in an account.
     *
     * @param name branch name
     * @param accountId account identifier
     * @return true if the branch name already exists
     */
    @Override
    public boolean existsByNameAndAccountId(String name, AccountId accountId) {
        return branchMongoRepository.existsByNameAndAccountId(name, accountId);
    }

    /**
     * Checks whether another branch with the same name exists in an account,
     * excluding the current branch.
     *
     * @param name branch name
     * @param accountId account identifier
     * @param id branch identifier to exclude
     * @return true if another branch with the same name exists
     */
    @Override
    public boolean existsByNameAndAccountIdAndIdNot(String name, AccountId accountId, String id) {
        return branchMongoRepository.existsByNameAndAccountIdAndIdNot(name, accountId, id);
    }
}