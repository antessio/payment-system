package antessio.paymentsystem.wallet.infrastructure.persistence;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;


@Entity
@Table(name = "wallet")
public class WalletEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "owner")
    private String owner;

    @Column(name = "ownerId")
    private String ownerId;

    @Column(name = "type")
    private String type;

    @Column(name = "amountUnit")
    private Long amountUnit;

    @CreationTimestamp
    @Column(name = "insertDate")
    private Date insertDate;

    @UpdateTimestamp
    @Column(name = "updateDate")
    private Date updateDate;

    @Version
    @Column(name = "version")
    private Long version;

    protected WalletEntity() {
    }

    WalletEntity(String id){
        this.id = id;
    }

    public WalletEntity(String id, String owner, String ownerId, String type, Long amountUnit, Date insertDate, Date updateDate, Long version) {
        this.id = id;
        this.owner = owner;
        this.ownerId = ownerId;
        this.type = type;
        this.amountUnit = amountUnit;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.version = version;
    }

    public WalletEntity(String owner, String ownerId, String type, Long amountUnit) {
        this.owner = owner;
        this.ownerId = ownerId;
        this.type = type;
        this.amountUnit = amountUnit;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getType() {
        return type;
    }

    public Long getAmountUnit() {
        return amountUnit;
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
