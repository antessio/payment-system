package antessio.paymentystem.bank.domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import com.github.f4b6a3.ulid.Ulid;

import antessio.paymentsystem.bank.BankTransferId;
import antessio.paymentsystem.bank.BankTransferStatus;
import antessio.paymentsystem.common.Amount;
import antessio.paymentsystem.topup.BankAccountId;

@AggregateRoot
public class BankTransfer {

    @Identity
    private final BankTransferId id;

    private final Amount amount;

    private final BankAccountId bankAccountId;

    private BankTransferStatus status;

    private LocalDate executionDate;

    private LocalDate reversalDate;

    public BankTransfer(Amount amount, BankAccountId bankAccountId) {
        this.id = new BankTransferId(Ulid.from(UUID.randomUUID()).toString());
        this.amount = amount;
        this.bankAccountId = bankAccountId;
        this.status = BankTransferStatus.REQUESTED;
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

    public Optional<LocalDate> getExecutionDate() {
        return Optional.ofNullable(executionDate);
    }

    public Optional<LocalDate> getReversalDate() {
        return Optional.ofNullable(reversalDate);
    }

    void execute() {
        execute(LocalDate.now(ZoneId.of("UTC")));
    }

    void execute(LocalDate executionDate) {
        this.status = BankTransferStatus.EXECUTED;
        this.executionDate = executionDate;
    }

    void revert(LocalDate reversalDate) {
        this.status = BankTransferStatus.REVERSED;
        this.reversalDate = reversalDate;
    }

    void revert() {
        this.revert(LocalDate.now(ZoneId.of("UTC")));
    }

    void fail() {
        this.status = BankTransferStatus.FAILED;
    }

    void cancel() {
        this.status = BankTransferStatus.CANCELED;
    }

}
