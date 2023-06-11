package antessio.paymentsystem.wallet;

public class MovementCreatedEvent {
    private MovementId id;
    private MovementDirection direction;
    private Amount amount;
    private WalletID fromWallet;
    private WalletType fromWalletType;

    public MovementCreatedEvent(
            MovementId id,
            MovementDirection direction,
            Amount amount,
            WalletID fromWallet,
            WalletType fromWalletType) {
        this.id = id;
        this.direction = direction;
        this.amount = amount;
        this.fromWallet = fromWallet;
        this.fromWalletType = fromWalletType;
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

}
