package antessio.paymentsystem.wallet;

public final class WalletBuilder {

    private WalletID id;
    private WalletOwner owner;
    private WalletOwnerId ownerId;
    private WalletType type;
    private Long amountUnit;

    private WalletBuilder() {
    }

    public static WalletBuilder aWallet() {
        return new WalletBuilder();
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

    public Wallet build() {
        return new Wallet(id, owner, ownerId, type, amountUnit);
    }

}
