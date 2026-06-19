package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
 * Action applied when resolving a conciliation task.
 */
public enum ConciliationResolutionAction {
    /**
     * Administrator confirms that the digital stock must be aligned with the
     * calculated total physical stock.
     */
    ADJUST_DIGITAL_STOCK,

    /**
     * Administrator confirms that stock exists outside the device and updates
     * the justified withdrawn stock value.
     */
    UPDATE_JUSTIFIED_WITHDRAWN_STOCK,

    /**
     * Administrator confirms that the discrepancy was caused by a device or
     * sensor problem that requires recalibration.
     */
    RECALIBRATE_DEVICE,

    /**
     * Backend closes the task automatically after a later stock comparison
     * shows that the discrepancy no longer exists.
     */
    AUTOMATIC_CLOSE
}
