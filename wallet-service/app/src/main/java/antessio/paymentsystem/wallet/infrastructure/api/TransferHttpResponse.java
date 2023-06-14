package antessio.paymentsystem.wallet.infrastructure.api;

import antessio.paymentsystem.wallet.Amount;

public class TransferHttpResponse {
    private String id;
    private String direction;
    private Amount amount;

    public TransferHttpResponse(String id, String direction, Amount amount) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public Amount getAmount() {
        return amount;
    }

}
