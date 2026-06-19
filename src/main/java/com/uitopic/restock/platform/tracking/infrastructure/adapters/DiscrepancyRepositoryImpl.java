package com.uitopic.restock.platform.tracking.infrastructure.adapters;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers.DiscrepancyPersistenceAssembler;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.DiscrepancyPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of the DiscrepancyRepository interface for managing inventory discrepancies.
 * This class uses a MongoDB repository for persistence operations and an assembler to convert between domain and persistence models.
 */
@Repository
@RequiredArgsConstructor
public class DiscrepancyRepositoryImpl implements DiscrepancyRepository {

    // MongoDB repository for persistence operations
    private final DiscrepancyPersistenceRepository discrepancyMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public Discrepancy save(Discrepancy discrepancy) {
        var saved = discrepancyMongoRepository
                .save(DiscrepancyPersistenceAssembler
                        .toPersistenceFromDomain(discrepancy));

        return DiscrepancyPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Discrepancy> findById(String id) {
        return discrepancyMongoRepository.findById(id)
                .map(DiscrepancyPersistenceAssembler::toDomainFromPersistence);
    }
}
