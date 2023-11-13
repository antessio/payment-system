package antessio.paymentsystem.topup.domain;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public class BankAccount {
    private BankAccountId bankAccountId;

    public BankAccount(BankAccountId bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BankAccountId getBankAccountId() {
        return bankAccountId;
    }

    public static class BankAccountId {
        private String id;

        public BankAccountId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

    }

}
