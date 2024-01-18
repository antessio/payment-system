package antessio.paymentsystem.bank.api;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.topup.BankAccountId;

public interface BankTransferApplicationServiceApi {

    BankTransferDTO createBankTransfer(Amount amount, BankAccountId bankAccountId);

    BankTransferDTO loadById(BankTransferId bankTransferId);

    PaginatedList<BankTransferDTO> loadByBankAccountId(BankAccountId bankAccountId, Integer limit, BankTransferId cursor);

    void executeBankTransfer(BankTransferId bankTransferId, LocalDate localDate);

    void executeBankTransfer(BankTransferId bankTransferId);

    void cancelBankTransfer(BankTransferId bankTransferId);


    void revertBankTransfer(BankTransferId bankTransferId, LocalDate localDate);

    void revertBankTransfer(BankTransferId bankTransferId);


}
