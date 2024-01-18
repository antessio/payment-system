package antessio.paymentsystem.bank.api;

import java.time.LocalDate;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.BankTransferStatus;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;

public class BankTransferDTO {

    private BankTransferId id;

    private Amount amount;

    private BankAccountId bankAccountId;

    private BankTransferStatus status;

    private LocalDate executionDate;

    private LocalDate reversalDate;

    public BankTransferDTO() {
    }

    public BankTransferDTO(
            BankTransferId id,
            Amount amount,
            BankAccountId bankAccountId,
            BankTransferStatus status,
            LocalDate executionDate,
            LocalDate reversalDate) {
        this.id = id;
        this.amount = amount;
        this.bankAccountId = bankAccountId;
        this.status = status;
        this.executionDate = executionDate;
        this.reversalDate = reversalDate;
    }

    public BankTransferId getId() {
        return id;
    }

    public Amount getAmount() {
        return amount;
    }

    public BankAccountId getBankAccountId() {
        return bankAccountId;
    }

    public BankTransferStatus getStatus() {
        return status;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public LocalDate getReversalDate() {
        return reversalDate;
    }

}
