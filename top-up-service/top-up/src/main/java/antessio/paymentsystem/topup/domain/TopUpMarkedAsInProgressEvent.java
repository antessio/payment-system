package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;

@DomainEvent
public abstract class TopUpMarkedAsInProgressEvent {
    private TopUp.TopUpId id;

    public TopUpMarkedAsInProgressEvent(TopUp.TopUpId id) {
        this.id = id;
    }
    public TopUp.TopUpId getId() {
        return id;
    }

}
