package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BankToWalletTopUpBankTransferCreatedEvent(TopUp.TopUpId topUpId, BankTransfer.BankTransferId bankTransferId) {

}
