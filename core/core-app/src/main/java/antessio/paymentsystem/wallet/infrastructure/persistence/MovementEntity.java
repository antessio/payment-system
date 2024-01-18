package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "movement", schema = "wallet")
public class MovementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "direction")
    private String direction;

    @Column(name = "amountUnit")
    private Long amountUnit;

    @Column(name = "amountCurrency")
    private String amountCurrency;

    @JoinColumn(name = "walletId")
    @OneToOne
    private WalletEntity wallet;

    @Column(name = "operationId")
    private String operationId;

    @Column(name = "insertDate")
    private Date insertDate;

    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "version")
    private Long version;


    protected MovementEntity() {
    }

    public MovementEntity(
            String id,
            String direction,
            Long amountUnit,
            String amountCurrency,
            WalletEntity wallet,
            String operationId,
            Date insertDate,
            Date updateDate,
            Long version) {
        this.id = id;
        this.direction = direction;
        this.amountUnit = amountUnit;
        this.amountCurrency = amountCurrency;
        this.wallet = wallet;
        this.operationId = operationId;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public WalletEntity getWallet() {
        return wallet;
    }
    public String getOperationId() {
        return operationId;
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
