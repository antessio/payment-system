package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BankToWalletTopUpCanceledEvent(TopUp.TopUpId topUpId, String reason) {

}
