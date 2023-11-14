package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BankToWalletTopUpWalletTransferCreatedEvent(TopUp.TopUpId topUpId, WalletTransfer.WalletTransferId walletTransferId) {

}
