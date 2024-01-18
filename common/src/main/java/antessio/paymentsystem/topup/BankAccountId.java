package antessio.paymentsystem.topup;

import java.util.UUID;

public class BankAccountId {

    private UUID id;

    public BankAccountId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

}
