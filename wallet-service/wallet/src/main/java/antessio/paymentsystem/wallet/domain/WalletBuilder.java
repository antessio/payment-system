package antessio.paymentsystem.wallet.domain;

import java.time.Instant;

import antessio.paymentsystem.wallet.WalletID;
import antessio.paymentsystem.wallet.WalletOwner;
import antessio.paymentsystem.wallet.WalletOwnerId;
import antessio.paymentsystem.wallet.WalletType;

public final class WalletBuilder {

    private Integer version;
    private WalletID id;
    private WalletOwner owner;
    private WalletOwnerId ownerId;
    private WalletType type;
    private Long amountUnit;
    private Instant insertOn;
    private Instant updatedOn;

    private WalletBuilder() {
    }

    public static WalletBuilder aWallet() {
        return new WalletBuilder();
    }

    public WalletBuilder withVersion(Integer version) {
        this.version = version;
        return this;
    }

    public WalletBuilder withId(WalletID id) {
        this.id = id;
        return this;
    }

    public WalletBuilder withOwner(WalletOwner owner) {
        this.owner = owner;
        return this;
    }

    public WalletBuilder withOwnerId(WalletOwnerId ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public WalletBuilder withType(WalletType type) {
        this.type = type;
        return this;
    }

    public WalletBuilder withAmountUnit(Long amountUnit) {
        this.amountUnit = amountUnit;
        return this;
    }

    public WalletBuilder withInsertOn(Instant insertOn) {
        this.insertOn = insertOn;
        return this;
    }

    public WalletBuilder withUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public Wallet build() {
        return new Wallet(id, owner, ownerId, type, amountUnit, insertOn, updatedOn,version);
    }

}
