package antessio.paymentsystem.bank.api;

import antessio.paymentsystem.topup.BankAccountId;

public class BankAccountDTO {

    private  BankAccountId id;
    private  String iban;
    private  String owner;

    public BankAccountDTO() {
    }

    public BankAccountDTO(BankAccountId id, String iban, String owner) {
        this.id = id;
        this.iban = iban;
        this.owner = owner;
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

}
