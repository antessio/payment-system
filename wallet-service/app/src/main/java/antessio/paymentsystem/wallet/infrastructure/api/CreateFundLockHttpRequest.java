package antessio.paymentsystem.wallet.infrastructure.api;

import antessio.paymentsystem.common.Amount;

public class CreateFundLockHttpRequest {
    private Amount amount;
    private String destinationOwner;

    public CreateFundLockHttpRequest(Amount amount, String destinationOwner) {
        this.amount = amount;
        this.destinationOwner = destinationOwner;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getDestinationOwner() {
        return destinationOwner;
    }

}
