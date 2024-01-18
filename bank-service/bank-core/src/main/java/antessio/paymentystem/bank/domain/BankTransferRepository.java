package antessio.paymentystem.bank.domain;

import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.topup.BankAccountId;

public interface BankTransferRepository {

    Optional<BankTransfer> loadById(BankTransferId id);

    Stream<BankTransfer> loadByBankAccountId(BankAccountId bankAccountId, BankAccountId startingFrom);

    void save(BankTransfer bankTransfer);

}
