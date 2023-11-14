package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BankToWalletTopUpCompletedEvent(TopUp.TopUpId topUpId, WalletTransfer walletTransfer) {

}
