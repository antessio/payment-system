package antessio.paymentsystem.topup;

public enum TopUpStatus {
    /**
     * Top-up requested
     */
    REQUESTED,
    /**
     * Top-up request is valid and it will be eventually executed.
     */
    IN_PROGRESS,
    /**
     * Top-up completed successfully
     */
    COMPLETED,
    /**
     * Top-up canceled before execution
     */
    CANCELED,
    /**
     * Top-up refunded after execution
     */
    REVERTED
}
