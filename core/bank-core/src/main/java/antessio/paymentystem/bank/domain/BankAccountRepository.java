package antessio.paymentystem.bank.domain;

import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.topup.BankAccountId;

public interface BankAccountRepository {
    Optional<BankAccount> loadById(BankAccountId bankAccountId);

    void save(BankAccount bankAccount);

    Optional<BankAccount> loadByIban(String iban);

    Stream<BankAccount> loadByOwner(String owner, BankAccountId startingFrom);

}
