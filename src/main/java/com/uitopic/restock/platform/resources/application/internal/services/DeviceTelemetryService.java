package com.uitopic.restock.platform.resources.application.internal.services;

/**
 * A small service inside the resources bounded context that is responsible for disabling telemetry
 * on devices attached to a specific branch when the branch is deleted.
 *
 * <p>This is a local concern within the resources bounded context — it does NOT call external
 * services or publish cross-BC events. Full device‑telemetry management lives in
 * the Device Management bounded context.
 */
public interface DeviceTelemetryService {

    /** Disable telemetry ingestion for all devices attached to the given branch id. */
    void disableTelemetryForBranch(String branchId);
}

