package antessio.paymentsystem.bank.api;

import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.topup.BankAccountId;

public interface BankAccountApplicationServiceApi {

    BankAccountDTO createBankAccount(String iban, String owner);

    BankAccountDTO loadById(BankAccountId bankAccountId);

    BankAccountDTO loadByIban(String iban);

    PaginatedList<BankAccountDTO> loadByOwner(String owner, Integer limit, BankAccountId startingFrom);

}
