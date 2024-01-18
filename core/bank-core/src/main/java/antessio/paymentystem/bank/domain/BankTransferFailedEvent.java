package antessio.paymentystem.bank.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.bank.BankTransferId;

@DomainEvent
public record BankTransferFailedEvent(BankTransferId bankTransferId) {

}
