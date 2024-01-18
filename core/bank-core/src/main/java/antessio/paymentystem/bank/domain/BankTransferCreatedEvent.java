package antessio.paymentystem.bank.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;

@DomainEvent
public record BankTransferCreatedEvent(BankTransferId id, Amount amount, BankAccountId bankAccountId) {


}
