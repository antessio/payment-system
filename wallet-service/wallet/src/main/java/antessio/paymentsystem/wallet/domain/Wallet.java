package antessio.paymentsystem.wallet.domain;

import java.time.Instant;

import antessio.paymentsystem.common.OptimisticLock;
import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;

public class Wallet extends OptimisticLock {
    private WalletID id;
    private WalletOwner owner;
    private WalletOwnerId ownerId;
    private WalletType type;
    private Long amountUnit;

    private Instant insertOn;

    private Instant updatedOn;

    public Wallet(
            WalletID id,
            WalletOwner owner,
            WalletOwnerId ownerId,
            WalletType type,
            Long amountUnit,
            Instant insertOn,
            Instant updatedOn,
            Integer version) {
        super(version);
        this.id = id;
        this.owner = owner;
        this.ownerId = ownerId;
        this.type = type;
        this.amountUnit = amountUnit;
        this.insertOn = insertOn;
        this.updatedOn = updatedOn;
    }

    public WalletID getId() {
        return id;
    }

    public WalletOwner getOwner() {
        return owner;
    }

    public WalletOwnerId getOwnerId() {
        return ownerId;
    }

    public WalletType getType() {
        return type;
    }

    public Long getAmountUnit() {
        return amountUnit;
    }


    public Instant getInsertOn() {
        return insertOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

}
