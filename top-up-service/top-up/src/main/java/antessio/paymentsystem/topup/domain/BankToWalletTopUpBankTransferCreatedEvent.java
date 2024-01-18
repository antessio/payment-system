package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.topup.TopUpId;

@DomainEvent
public record BankToWalletTopUpBankTransferCreatedEvent(TopUpId topUpId, BankTransferId bankTransferId) {

}
