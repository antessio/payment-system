package antessio.paymentsystem.topup.domain;

import java.util.UUID;

import org.jmolecules.ddd.annotation.Identity;

import antessio.paymentsystem.common.Amount;

/**
 * A generic top-up
 */
public abstract class TopUp {

    @Identity
    private TopUpId id;
    private Amount amount;

    private TopUpStatus status;

    TopUp(TopUpId id, Amount amount) {
        this.id = id;
        this.amount = amount;
        this.status = TopUpStatus.REQUESTED;
    }

    /**
     * Top-up identifier
     * @return
     */
    public TopUpId getId() {
        return id;
    }

    /**
     * Top-up amount
     * @return
     */
    public Amount getAmount() {
        return amount;
    }

    public TopUpStatus getStatus() {
        return status;
    }

    protected void complete(){
        this.status = TopUpStatus.COMPLETED;
    }
    protected void cancel(){
        this.status = TopUpStatus.CANCELED;
    }
    void markAsInProgress(){
        this.status = TopUpStatus.IN_PROGRESS;
    }


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

    /**
     * Top-Up identifier
     */
    public static class TopUpId {

        private UUID id;

        public TopUpId(UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }

        /**
         * Generate a new random top-up id
         * @return
         */
        public static TopUpId generate() {
            return new TopUpId(UUID.randomUUID());
        }

        /**
         * Convert a string to a top-up id
         * @param id
         * @return
         */
        public static TopUpId fromString(String id) {
            return new TopUpId(UUID.fromString(id));
        }

    }

}
