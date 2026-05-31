package com.uitopic.restock.platform.resources.application.internal.services;

/**
 * A small service inside the resources bounded context that is responsible for disabling telemetry
 * for devices belonging to a branch. For now it only logs the intent — in a later iteration this
 * can call the devices/tracking bounded context or an integration layer.
 */
public interface DeviceTelemetryService {

    /** Disable telemetry ingestion for all devices attached to the given branch id. */
    void disableTelemetryForBranch(String branchId);
}

