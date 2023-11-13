package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;

@DomainEvent
public abstract class TopUpRequestedEvent {
    private TopUp.TopUpId id;
    private Amount amount;

    public TopUpRequestedEvent(TopUp.TopUpId id, Amount amount) {
        this.id = id;
        this.amount = amount;
    }

    public TopUp.TopUpId getId() {
        return id;
    }

    public Amount getAmount() {
        return amount;
    }

}
