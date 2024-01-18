package antessio.paymentystem.bank.domain;

import java.util.UUID;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import antessio.paymentsystem.topup.BankAccountId;

@AggregateRoot
public class BankAccount {

    @Identity
    private final BankAccountId id;
    private final String iban;
    private final String owner;

    private final Long version;

    public BankAccount(String iban, String owner) {
        this.id = new BankAccountId(UUID.randomUUID());
        this.iban = iban;
        this.owner = owner;
        this.version = 0L;
    }

    public BankAccountId getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getOwner() {
        return owner;
    }

    public Long getVersion() {
        return version;
    }

}
