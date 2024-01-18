package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.TopUpId;

@DomainEvent
public record BankToWalletTopUpRequestedEvent(TopUpId id, Amount amount, Wallet wallet, BankAccount bankAccount) {

}
