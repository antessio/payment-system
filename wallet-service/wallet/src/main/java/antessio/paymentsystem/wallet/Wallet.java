package antessio.paymentsystem.wallet;

public class Wallet {
    private WalletID id;
    private WalletOwner owner;

    private WalletOwnerId ownerId;
    private WalletType type;
    private Long amountUnit;

    public Wallet(WalletID id, WalletOwner owner, WalletOwnerId ownerId, WalletType type, Long amountUnit) {
        this.id = id;
        this.owner = owner;
        this.ownerId = ownerId;
        this.type = type;
        this.amountUnit = amountUnit;
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

}
