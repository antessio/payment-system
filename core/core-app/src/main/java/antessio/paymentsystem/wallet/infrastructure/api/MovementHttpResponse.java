package antessio.paymentsystem.wallet.infrastructure.api;

import antessio.paymentsystem.common.Amount;

public class MovementHttpResponse {
    private String id;
    private String direction;
    private Amount amount;

    public MovementHttpResponse(String id, String direction, Amount amount) {
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
