package com.uitopic.restock.platform.tracking.domain.repositories;

import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing discrepancy entities, providing methods for saving and retrieving inventory discrepancies detected from telemetry readings. This interface abstracts the underlying data storage mechanism, allowing for flexibility in how discrepancies are persisted and accessed within the application.
 */
public interface DiscrepancyRepository {

    /**
     * Saves a discrepancy entity to the repository, allowing for the persistence of inventory discrepancies detected from telemetry readings. This method is responsible for storing the discrepancy information, which can be used for further analysis and reporting.
     *
     * @param discrepancy the discrepancy entity to be saved, containing information about the detected inventory discrepancy, including the device ID, physical stock level, temperature, humidity, and timestamp of the reading that triggered the discrepancy
     * @return the saved discrepancy entity, which may include additional information such as a generated unique identifier or timestamps for when the discrepancy was created and last updated
     */
    Discrepancy save(Discrepancy discrepancy);

    /**
     * Finds a discrepancy by its identifier.
     *
     * @param id discrepancy identifier
     * @return optional discrepancy if found
     */
    Optional<Discrepancy> findById(String id);

    /**
     * Finds discrepancies using optional filters.
     *
     * @param status optional discrepancy status filter
     * @return list of matching discrepancies
     */
    List<Discrepancy> findAllByFilters(DiscrepancyStatus status);
}
