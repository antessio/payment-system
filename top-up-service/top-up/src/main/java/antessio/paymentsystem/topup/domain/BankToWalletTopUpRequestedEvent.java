package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;

@DomainEvent
public record BankToWalletTopUpRequestedEvent(TopUp.TopUpId id, Amount amount, Wallet wallet, BankAccount bankAccount) {

}
