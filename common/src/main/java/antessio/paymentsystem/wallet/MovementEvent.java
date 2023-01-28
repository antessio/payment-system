package antessio.paymentsystem.wallet;

public class MovementEvent {
    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID fromWallet;
    private WalletType fromWalletType;

    private WalletID toWallet;
    private WalletType toWalletType;

    public MovementEvent(
            MovementId id,
            MovementDirection direction,
            Amount amount,
            WalletID fromWallet,
            WalletType fromWalletType,
            WalletID toWallet,
            WalletType toWalletType) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.fromWallet = fromWallet;
        this.fromWalletType = fromWalletType;
        this.toWallet = toWallet;
        this.toWalletType = toWalletType;
    }

    public MovementId getId() {
        return id;
    }

    public MovementDirection getDirection() {
        return direction;
    }

    public Amount getAmount() {
        return amount;
    }

    public WalletID getFromWallet() {
        return fromWallet;
    }

    public WalletType getFromWalletType() {
        return fromWalletType;
    }

    public WalletID getToWallet() {
        return toWallet;
    }

    public WalletType getToWalletType() {
        return toWalletType;
    }

}
