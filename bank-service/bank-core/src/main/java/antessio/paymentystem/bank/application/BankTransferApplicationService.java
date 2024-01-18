package antessio.paymentystem.bank.application;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Stream;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.api.BankTransferDTO;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;
import antessio.paymentystem.bank.domain.BankTransfer;
import antessio.paymentystem.bank.domain.BankTransferDomainService;
import antessio.paymentystem.bank.domain.BankTransferRepository;

public class BankTransferApplicationService  {

    private final BankTransferDomainService bankTransferDomainService;
    private final BankTransferRepository bankTransferRepository;


    public BankTransferApplicationService(
            BankTransferDomainService bankTransferDomainService, BankTransferRepository bankTransferRepository) {
        this.bankTransferDomainService = bankTransferDomainService;
        this.bankTransferRepository = bankTransferRepository;
    }



    public BankTransferDTO createBankTransfer(Amount amount, BankAccountId bankAccountId) {
        BankTransfer bankTransfer = bankTransferDomainService.createBankTransfer(amount, bankAccountId);
        return toDTO(bankTransfer);
    }

    public Optional<BankTransferDTO> loadById(BankTransferId bankTransferId) {
        return bankTransferRepository.loadById(bankTransferId)
                                     .map(BankTransferApplicationService::toDTO);
    }

    public Stream<BankTransferDTO> loadByBankAccountId(BankAccountId bankAccountId, BankAccountId startingFrom) {
        return bankTransferRepository.loadByBankAccountId(bankAccountId, startingFrom)
                                     .map(BankTransferApplicationService::toDTO);
    }

    public void executeBankTransfer(BankTransferId bankTransferId, LocalDate localDate) {
        bankTransferDomainService.executeBankTransfer(loadBankTransfer(bankTransferId), localDate);

    }

    public void executeBankTransfer(BankTransferId bankTransferId) {
        executeBankTransfer(bankTransferId, now());
    }

    public void cancelBankTransfer(BankTransferId bankTransferId) {
        bankTransferDomainService.cancelBankTransfer(loadBankTransfer(bankTransferId));
    }

    public void revertBankTransfer(BankTransferId bankTransferId, LocalDate localDate) {
        bankTransferDomainService.revertBankTransfer(loadBankTransfer(bankTransferId),localDate);
    }

    public void revertBankTransfer(BankTransferId bankTransferId) {
        revertBankTransfer(bankTransferId, now());
    }

    private static LocalDate now() {
        return LocalDate.now(ZoneId.of("UTC"));
    }


    private static BankTransferDTO toDTO(BankTransfer bankTransfer) {
        return new BankTransferDTO(
                bankTransfer.getId(),
                bankTransfer.getAmount(),
                bankTransfer.getBankAccountId(),
                bankTransfer.getStatus(),
                bankTransfer.getExecutionDate().orElse(null),
                bankTransfer.getReversalDate().orElse(null));
    }

    private BankTransfer loadBankTransfer(BankTransferId bankTransferId) {
        BankTransfer bankTransfer = bankTransferRepository.loadById(bankTransferId)
                                                          .orElseThrow(() -> new IllegalArgumentException("bank transfer %s not found".formatted(bankTransferId.id())));
        return bankTransfer;
    }

}
