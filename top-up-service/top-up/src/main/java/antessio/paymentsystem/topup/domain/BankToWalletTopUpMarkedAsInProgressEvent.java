package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;

@DomainEvent
public class BankToWalletTopUpMarkedAsInProgressEvent extends TopUpMarkedAsInProgressEvent{

    public BankToWalletTopUpMarkedAsInProgressEvent(TopUp.TopUpId id) {
        super(id);
    }
}
