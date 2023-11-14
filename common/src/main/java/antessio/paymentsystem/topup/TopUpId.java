package antessio.paymentsystem.topup;

import java.util.UUID;

/**
 * Top-Up identifier
 */
public class TopUpId {

    private UUID id;

    public TopUpId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    /**
     * Generate a new random top-up id
     *
     * @return
     */
    public static TopUpId generate() {
        return new TopUpId(UUID.randomUUID());
    }

    /**
     * Convert a string to a top-up id
     *
     * @param id
     *
     * @return
     */
    public static TopUpId fromString(String id) {
        return new TopUpId(UUID.fromString(id));
    }

}
