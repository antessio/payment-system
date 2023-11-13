package antessio.paymentsystem.topup.domain;

import org.jmolecules.event.annotation.DomainEvent;

import antessio.paymentsystem.common.Amount;

@DomainEvent
public class BankToWalletTopUpRequestedEvent extends TopUpRequestedEvent {
    private Wallet wallet;
    private BankAccount bankAccount;

    public BankToWalletTopUpRequestedEvent(TopUp.TopUpId id, Amount amount, Wallet wallet, BankAccount bankAccount) {
        super(id, amount);
        this.wallet = wallet;
        this.bankAccount = bankAccount;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

}
