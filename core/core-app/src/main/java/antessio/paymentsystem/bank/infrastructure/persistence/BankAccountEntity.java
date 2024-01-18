package antessio.paymentsystem.bank.infrastructure.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "bank-account")
public class BankAccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "iban")
    private String iban;

    @Column(name = "owner")
    private String owner;

    @Column(name = "insertDate")
    private Date insertDate;

    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "version")
    private Long version;


    protected BankAccountEntity() {
    }

    public BankAccountEntity(UUID id, String iban, String owner, Date insertDate, Date updateDate, Long version) {
        this.id = id;
        this.iban = iban;
        this.owner = owner;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getOwner() {
        return owner;
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
