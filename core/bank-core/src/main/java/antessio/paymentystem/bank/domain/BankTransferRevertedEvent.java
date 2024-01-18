package antessio.paymentystem.bank.domain;

import java.time.LocalDate;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.bank.BankTransferId;

@DomainEvent
public record BankTransferRevertedEvent(BankTransferId bankTransferId, LocalDate revertDate) {

}
