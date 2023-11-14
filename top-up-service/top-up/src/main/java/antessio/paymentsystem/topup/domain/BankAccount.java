package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import antessio.paymentsystem.topup.BankAccountId;

@ValueObject
public class BankAccount {
    private BankAccountId bankAccountId;

    public BankAccount(BankAccountId bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BankAccountId getBankAccountId() {
        return bankAccountId;
    }

}
