package antessio.paymentsystem.topup.dto;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.TopUpId;
import antessio.paymentsystem.topup.TopUpStatus;

public class TopUpDto {
    private TopUpId id;
    private Amount amount;
    private TopUpStatus status;

    public TopUpDto() {
    }

    public TopUpDto(TopUpId id, Amount amount, TopUpStatus status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public TopUpId getId() {
        return id;
    }

    public Amount getAmount() {
        return amount;
    }

    public TopUpStatus getStatus() {
        return status;
    }

}
