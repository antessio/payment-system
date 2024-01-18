package antessio.paymentsystem.bank.infrastructure.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import antessio.paymentsystem.bank.BankTransferStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "bank-transfer")
public class BankTransferEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "amountUnit")
    private Long amountUnit;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BankTransferStatus status;
    @Column(name = "bankAccountId")
    private UUID bankAccountId;

    @Column(name = "executionDate")
    private LocalDate executionDate;
    @Column(name = "reversalDate")
    private LocalDate reversalDate;

    @Column(name = "insertDate")
    private Date insertDate;

    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "version")
    private Long version;


    protected BankTransferEntity() {
    }

    public BankTransferEntity(
            String id,
            Long amountUnit,
            String currency,
            BankTransferStatus status,
            UUID bankAccountId,
            LocalDate executionDate,
            LocalDate reversalDate,
            Date insertDate,
            Date updateDate,
            Long version) {
        this.id = id;
        this.amountUnit = amountUnit;
        this.currency = currency;
        this.status = status;
        this.bankAccountId = bankAccountId;
        this.executionDate = executionDate;
        this.reversalDate = reversalDate;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public BankTransferStatus getStatus() {
        return status;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public LocalDate getReversalDate() {
        return reversalDate;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Long getVersion() {
        return version;
    }

}
