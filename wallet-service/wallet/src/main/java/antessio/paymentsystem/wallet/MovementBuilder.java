package antessio.paymentsystem.wallet;

public final class MovementBuilder {

    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID fromWallet;
    private WalletType fromWalletType;
    private WalletID toWallet;
    private WalletType toWalletType;

    private MovementBuilder() {
    }

    public static MovementBuilder aMovement() {
        return new MovementBuilder();
    }

    public MovementBuilder withId(MovementId id) {
        this.id = id;
        return this;
    }

    public MovementBuilder withDirection(MovementDirection direction) {
        this.direction = direction;
        return this;
    }

    public MovementBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public MovementBuilder withFromWallet(WalletID fromWallet) {
        this.fromWallet = fromWallet;
        return this;
    }

    public MovementBuilder withFromWalletType(WalletType fromWalletType) {
        this.fromWalletType = fromWalletType;
        return this;
    }

    public MovementBuilder withToWallet(WalletID toWallet) {
        this.toWallet = toWallet;
        return this;
    }

    public MovementBuilder withToWalletType(WalletType toWalletType) {
        this.toWalletType = toWalletType;
        return this;
    }

    public Movement build() {
        return new Movement(id, direction, amount, fromWallet, fromWalletType, toWallet, toWalletType);
    }

}
