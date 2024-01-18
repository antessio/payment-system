package antessio.paymentsystem.bank.infrastructure.api;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.api.BankTransferApplicationServiceApi;
import antessio.paymentsystem.bank.api.BankTransferDTO;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.common.PaginatedList;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.application.BankTransferApplicationService;

@RestController
@RequestMapping("/api/bankTransfer")
public class BankTransferController implements BankTransferApplicationServiceApi {

    private final BankTransferApplicationService bankTransferApplicationService;

    public BankTransferController(BankTransferApplicationService bankTransferApplicationService) {
        this.bankTransferApplicationService = bankTransferApplicationService;
    }

    @Override
    public BankTransferDTO createBankTransfer(Amount amount, BankAccountId bankAccountId) {
        return null;
    }

    @Override
    public BankTransferDTO loadById(BankTransferId bankTransferId) {
        return null;
    }

    @Override
    public PaginatedList<BankTransferDTO> loadByBankAccountId(BankAccountId bankAccountId, Integer limit, BankTransferId cursor) {
        return null;
    }

    @Override
    public void executeBankTransfer(BankTransferId bankTransferId, LocalDate localDate) {

    }

    @Override
    public void executeBankTransfer(BankTransferId bankTransferId) {

    }

    @Override
    public void cancelBankTransfer(BankTransferId bankTransferId) {

    }

    @Override
    public void revertBankTransfer(BankTransferId bankTransferId, LocalDate localDate) {

    }

    @Override
    public void revertBankTransfer(BankTransferId bankTransferId) {

    }

}
